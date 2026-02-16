#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

services=(eureka-server api-gateway ms-users ms-masterdata ms-logistics)

for service in "${services[@]}"; do
  echo "[build] $service"
  (cd "$service" && mvn -q -DskipTests clean package)
done

echo "[docker] levantando stack"
docker compose up -d --build

echo "[wait] gateway http://localhost:8080/actuator/health"
for _ in {1..90}; do
  if curl -fsS http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo "[ok] stack listo"
    exit 0
  fi
  sleep 2
done

echo "[error] gateway no responde a tiempo"
exit 1
