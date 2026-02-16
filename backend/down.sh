#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

echo "[docker] bajando stack"
docker compose down -v --remove-orphans
