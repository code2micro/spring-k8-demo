#!/bin/bash
# deploy-service.sh <name> <port> [target-port]
set -e

SVC=$1
PORT=${2:-8080}
TARGET=${3:-$PORT}

if [ -z "$SVC" ]; then
    echo "Usage: $0 <service-name> <port> [target-port]"
    exit 1
fi

echo "🧹 Cleaning $SVC..."
kubectl delete deployment "$SVC" --ignore-not-found=true --force --grace-period=0
kubectl delete service "$SVC" --ignore-not-found=true
kubectl delete rs -l app="$SVC" --ignore-not-found=true
sleep 8

echo "🚀 Deploying $SVC..."
kubectl create deployment "$SVC" --image="$SVC:latest"
kubectl patch deployment "$SVC" -p "{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"$SVC\",\"imagePullPolicy\":\"Never\",\"env\":[{\"name\":\"JAVA_TOOL_OPTIONS\",\"value\":\"-Xmx128m\"}]}]}}}}"
kubectl expose deployment "$SVC" --type=NodePort --port="$PORT" --target-port="$TARGET"

echo "⏳ Waiting for $SVC to be ready..."
for i in {1..40}; do
    ready=$(kubectl get deployment "$SVC" -o jsonpath='{.status.readyReplicas}' 2>/dev/null || echo 0)
    if [ "$ready" = "1" ]; then
        echo "✅ $SVC is Ready!"
        exit 0
    fi
    sleep 2
done

echo "⚠️  $SVC did not become ready in 80s"
kubectl get pods | grep "$SVC"
exit 1
