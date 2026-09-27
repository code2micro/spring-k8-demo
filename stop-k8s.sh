#!/bin/bash
# stop-k8s.sh — Delete all deployments and stop K3s to free RAM
# Usage: ./stop-k8s.sh

SERVICES=("api-gateway" "inventory-service" "order-service")

echo "🧹 Deleting deployments and services..."
for svc in "${SERVICES[@]}"; do
    kubectl delete deployment "$svc" --ignore-not-found=true &>/dev/null
    kubectl delete service "$svc" --ignore-not-found=true &>/dev/null
done

echo "🛑 Stopping K3s..."
sudo systemctl stop k3s

echo "🧼 Running deep clean (kills leftover shims)..."
sudo /usr/local/bin/k3s-killall.sh &>/dev/null || true

echo ""
echo "💾 Memory after cleanup:"
free -h
echo ""
echo "✅ All stopped. Your laptop should feel faster now."
