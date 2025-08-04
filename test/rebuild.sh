#!/bin/sh
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
  kubectl wait --for=condition=Ready pod -l app=agones -n agones-system --timeout=120s
  echo "✅ Agones is ready on K3d cluster $TEST_CLUSTER_NAME."
fi

if ! helm list | grep -q cheetah; then
  echo "📦 Octopus not installed on K3d cluster $TEST_CLUSTER_NAME. Installing it..."
  echo $GITHUB_TOKEN | helm registry login ghcr.io -u $GITHUB_USERNAME --password-stdin
  helm install octopus oci://ghcr.io/o7studios/octopus-chart
  echo "✅ Octopus installed on K3d cluster $TEST_CLUSTER_NAME."
  sleep 3s
  echo "⏳ Waiting until Octopus is ready on K3d cluster $TEST_CLUSTER_NAME..."
  kubectl wait --for=condition=Ready pod -l app.kubernetes.io/instance=octopus-chart --timeout=240s
  echo "✅ Octopus is ready on K3d cluster $TEST_CLUSTER_NAME."
fi

if ! helm list | grep -q cheetah; then
  echo "📦 Cheetah not installed on K3d cluster $TEST_CLUSTER_NAME. Installing it..."
  echo $GITHUB_TOKEN | helm registry login ghcr.io -u $GITHUB_USERNAME --password-stdin
  helm install cheetah oci://ghcr.io/o7studios/cheetah-chart
  echo "✅ Cheetah installed on K3d cluster $TEST_CLUSTER_NAME."
  sleep 3s
  echo "⏳ Waiting until Cheetah is ready on K3d cluster $TEST_CLUSTER_NAME..."
  kubectl wait --for=condition=Ready pod -l app.kubernetes.io/instance=cheetah-chart --timeout=240s
  echo "✅ Cheetah is ready on K3d cluster $TEST_CLUSTER_NAME."
fi

if ! helm list | grep -q geyser; then
  echo "📦 Geyser not installed on K3d cluster $TEST_CLUSTER_NAME. Installing it..."
  echo $GITHUB_TOKEN | helm registry login ghcr.io -u $GITHUB_USERNAME --password-stdin
  helm install geyser oci://ghcr.io/o7studios/geyser-chart
  echo "✅ Geyser installed on K3d cluster $TEST_CLUSTER_NAME."
  sleep 3s
  echo "⏳ Waiting until Geyser is ready on K3d cluster $TEST_CLUSTER_NAME..."
  kubectl wait --for=condition=Ready pod -l app.kubernetes.io/instance=geyser-chart --timeout=240s
  echo "✅ Geyser is ready on K3d cluster $TEST_CLUSTER_NAME."
fi

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
helm install "$IMAGE_NAME" ./chart -n "default"
echo "✅ Installed helm chart on K3d cluster $TEST_CLUSTER_NAME"