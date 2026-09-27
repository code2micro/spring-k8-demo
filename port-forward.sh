#!/bin/bash
# port-forward.sh
kubectl port-forward --address 127.0.0.1 svc/api-gateway 8080:8080
