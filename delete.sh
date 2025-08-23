#!/bin/bash
set -e

echo "🗑 Deleting K3d cluster $TEST_CLUSTER_NAME..."
k3d cluster delete $TEST_CLUSTER_NAME
echo "✅ Deleted K3d cluster $TEST_CLUSTER_NAME..."