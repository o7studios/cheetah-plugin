#!/bin/bash
set -e

API_PORT=6550
API_HOST=host.docker.internal

if ! k3d cluster list | grep -qw "$TEST_CLUSTER_NAME"; then
  echo "🔧 K3d cluster $TEST_CLUSTER_NAME not found. Creating it..."
  k3d cluster create "$TEST_CLUSTER_NAME" --api-port "$API_PORT" -p "25565:25565@loadbalancer" -p "19132:19132/udp@loadbalancer"
  echo "✅ K3d cluster $TEST_CLUSTER_NAME created."
fi

rm ~/.kube -fr

echo "🔧 Ensuring K3d cluster $TEST_CLUSTER_NAME is online..."
k3d cluster start "$TEST_CLUSTER_NAME" --wait
echo "✅ K3d cluster $TEST_CLUSTER_NAME online."

echo "🔧 Merging kubeconfig & set context..."
k3d kubeconfig merge "$TEST_CLUSTER_NAME" \
  --kubeconfig-merge-default \
  --kubeconfig-switch-context

echo "🔧 Rewriting kubeconfig server URL to $API_HOST:$API_PORT."
kubectl config set-cluster k3d-"$TEST_CLUSTER_NAME" \
  --server="https://$API_HOST:$API_PORT" \
  --insecure-skip-tls-verify=true

kubectl config use-context k3d-"$TEST_CLUSTER_NAME"

if ! kubectl get secret github-token >/dev/null 2>&1; then
  echo "🔐 Creating secret for github-token on K3d cluster $TEST_CLUSTER_NAME..."
  kubectl create secret generic github-token --from-literal="GITHUB_TOKEN=$GITHUB_TOKEN"
  echo "✅ Created secret for github-token on K3d cluster $TEST_CLUSTER_NAME..."
fi

if ! kubectl get secret ghcr-secret >/dev/null 2>&1; then
  echo "🔐 Creating secret for docker-registry on K3d cluster $TEST_CLUSTER_NAME..."
  kubectl create secret docker-registry ghcr-secret \
    --docker-server=ghcr.io \
    --docker-username="$GITHUB_USERNAME" \
    --docker-password="$GITHUB_TOKEN"
  echo "✅ Created secret for docker-registry on K3d cluster $TEST_CLUSTER_NAME."
fi

if ! helm list -n agones-system | grep -q agones; then
  echo "📦 Agones not installed on K3d cluster $TEST_CLUSTER_NAME. Installing it..."
  helm repo add agones https://agones.dev/chart/stable
  helm repo update
  helm install agones --namespace agones-system --create-namespace agones/agones
  echo "✅ Agones installed on K3d cluster $TEST_CLUSTER_NAME."
  sleep 3s
  echo "⏳ Waiting until Agones is ready on K3d cluster $TEST_CLUSTER_NAME..."
  kubectl wait --for=condition=Ready pod -l app=agones -n agones-system --timeout=300s
  echo "✅ Agones is ready on K3d cluster $TEST_CLUSTER_NAME."
fi

if ! kubectl get secret octopus-ca-cert >/dev/null 2>&1; then
  echo "🔐 Creating CA secret..."
  CA_KEY=$(openssl genrsa 4096)
  CA_CRT=$(echo "$CA_KEY" | \
    openssl req -x509 -new -nodes -key /dev/stdin -sha256 -days 3650 -subj "/CN=Octopus-CA")

  kubectl create secret generic octopus-ca-cert \
    --from-literal=ca.crt="$CA_CRT"

  echo "✅ Created secret octopus-ca-cert"
fi

if ! kubectl get secret octopus-server-cert >/dev/null 2>&1; then
  echo "🔐 Creating server cert secret..."
  SERVER_KEY=$(openssl genrsa 4096)

  SERVER_CSR=$(echo "$SERVER_KEY" | openssl req -new -key /dev/stdin -subj "/CN=octopus" \
    -addext "subjectAltName=DNS:octopus,DNS:octopus.default.svc.cluster.local,IP:127.0.0.1")

  SERVER_CRT=$(echo "$SERVER_CSR" | openssl x509 -req \
    -CA <(echo "$CA_CRT") \
    -CAkey <(echo "$CA_KEY") \
    -CAserial <(echo 01) \
    -days 365 -sha256 \
    -extfile <(printf "subjectAltName=DNS:octopus,DNS:octopus.default.svc.cluster.local,IP:127.0.0.1"))

  kubectl create secret generic octopus-server-cert \
    --from-literal=server.key="$SERVER_KEY" \
    --from-literal=server.crt="$SERVER_CRT"


  echo "✅ Created secret octopus-server-cert"
fi

if ! kubectl get secret octopus-client-cert >/dev/null 2>&1; then
  echo "🔐 Creating client cert secret..."
  CLIENT_KEY=$(openssl genrsa 4096)

  CLIENT_CSR=$(echo "$CLIENT_KEY" | openssl req -new -key /dev/stdin -subj "/CN=octopus-client" \
    -addext "subjectAltName=DNS:octopus-client")

  CLIENT_CRT=$(echo "$CLIENT_CSR" | openssl x509 -req \
    -CA <(echo "$CA_CRT") \
    -CAkey <(echo "$CA_KEY") \
    -CAserial <(echo 01) \
    -days 365 -sha256 \
    -extfile <(printf "subjectAltName=DNS:octopus-client"))

  kubectl create secret generic octopus-client-cert \
    --from-literal=client.key="$CLIENT_KEY" \
    --from-literal=client.crt="$CLIENT_CRT"

  echo "✅ Created secret octopus-client-cert"
fi

if ! helm list | grep -q octopus; then
  echo "📦 Octopus not installed on K3d cluster $TEST_CLUSTER_NAME. Installing it..."
  echo $GITHUB_TOKEN | helm registry login ghcr.io -u $GITHUB_USERNAME --password-stdin
  helm install octopus oci://ghcr.io/o7studios/charts/octopus
  echo "✅ Octopus installed on K3d cluster $TEST_CLUSTER_NAME."
  sleep 3s
  echo "⏳ Waiting until Octopus is ready on K3d cluster $TEST_CLUSTER_NAME..."
  kubectl wait --for=condition=Ready pod -l app.kubernetes.io/instance=octopus --timeout=240s
  echo "✅ Octopus is ready on K3d cluster $TEST_CLUSTER_NAME."
fi

gradle shadowJar

IMAGE_NAME=ctest

echo "🛠 Building docker image $IMAGE_NAME..."
docker build --secret id=GITHUB_TOKEN --secret id=GITHUB_USERNAME -t $IMAGE_NAME:latest .
echo "✅ Finished building docker image."

echo "📥 Importing docker image $IMAGE_NAME onto K3d cluster $TEST_CLUSTER_NAME..."
k3d image import $IMAGE_NAME:latest -c $TEST_CLUSTER_NAME
echo "✅ Imported docker image $IMAGE_NAME onto K3d cluster $TEST_CLUSTER_NAME."

echo "💉 Installing helm chart on K3d cluster $TEST_CLUSTER_NAME"
helm uninstall "$IMAGE_NAME" -n "default" --ignore-not-found
echo $GITHUB_TOKEN | helm registry login ghcr.io -u $GITHUB_USERNAME --password-stdin
helm install "$IMAGE_NAME" ./test/chart -n "default"
echo "✅ Installed helm chart on K3d cluster $TEST_CLUSTER_NAME"