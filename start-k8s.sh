#!/bin/bash
# start-k8s.sh — Start K3s and deploy all three microservices
# Usage: ./start-k8s.sh

set -e

SERVICES=("api-gateway" "inventory-service" "order-service")
PORTS=("8080:8080" "8080:8080" "8081:8081")

echo "🚀 Step 1: Starting K3s..."
sudo systemctl start k3s

echo "⏳ Waiting for K3s to be ready..."
for i in {1..30}; do
    if kubectl get nodes &>/dev/null; then
        echo "✅ K3s is up!"
        break
    fi
    sleep 2
done

echo ""
echo "🧹 Step 2: Cleaning any old deployments..."
for svc in "${SERVICES[@]}"; do
    kubectl delete deployment "$svc" --ignore-not-found=true &>/dev/null
    kubectl delete service "$svc" --ignore-not-found=true &>/dev/null
done
sleep 5

echo ""
echo "📦 Step 3: Deploying services with 128MB memory limits..."
for i in "${!SERVICES[@]}"; do
    svc="${SERVICES[$i]}"
    port="${PORTS[$i]}"
    
    echo "  → Deploying $svc (port $port)..."
    kubectl create deployment "$svc" --image="$svc:latest"
    kubectl patch deployment "$svc" -p "{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"$svc\",\"imagePullPolicy\":\"Never\",\"env\":[{\"name\":\"JAVA_TOOL_OPTIONS\",\"value\":\"-Xmx128m\"}]}]}}}}"
    kubectl expose deployment "$svc" --type=NodePort --port="${port%%:*}" --target-port="${port##*:}"
done

echo ""
echo "⏳ Waiting for pods to be Running (max 90s)..."
for i in {1..45}; do
    ready=$(kubectl get pods --no-headers 2>/dev/null | grep -c "Running")
    if [ "$ready" -eq "${#SERVICES[@]}" ]; then
        echo "✅ All $ready pods are Running!"
        break
    fi
    sleep 2
done

echo ""
kubectl get pods
echo ""
echo "💾 Memory status:"
free -h
echo ""
echo "✅ Done! To access the services, run these in separate terminals:"
echo "   kubectl port-forward svc/api-gateway 8080:8080"
echo ""
echo "🛑 To stop everything: ./stop-k8s.sh"
