#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080/api}"
RUN_ID="$(date +%s)"
TRANSPORTISTA_EMAIL="luis.transportista.${RUN_ID}@daw.com"

extract_id() {
  echo "$1" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p' | head -n 1
}

api_call() {
  local method="$1"
  local url="$2"
  local data="${3:-}"

  for _ in {1..30}; do
    if [ -n "$data" ]; then
      RESPONSE=$(curl -sS -w '\n%{http_code}' -X "$method" "$url" -H 'Content-Type: application/json' -d "$data" || true)
    else
      RESPONSE=$(curl -sS -w '\n%{http_code}' -X "$method" "$url" || true)
    fi

    BODY=$(echo "$RESPONSE" | sed '$d')
    CODE=$(echo "$RESPONSE" | tail -n 1)

    if [ "$CODE" -ge 200 ] 2>/dev/null && [ "$CODE" -lt 300 ] 2>/dev/null; then
      echo "$BODY"
      return 0
    fi

    sleep 2
  done

  echo "Request failed: $method $url" >&2
  echo "Last response code: ${CODE:-N/A}" >&2
  echo "Last body: ${BODY:-N/A}" >&2
  exit 1
}

echo "Esperando API Gateway en http://localhost:8080/actuator/health ..."
for _ in {1..90}; do
  if curl -fsS http://localhost:8080/actuator/health >/dev/null 2>&1; then
    break
  fi
  sleep 2
done

echo "1) Login admin"
LOGIN=$(api_call POST "$BASE_URL/users/login" '{"email":"admin@daw.com","password":"admin123"}')
echo "$LOGIN"
ADMIN_ID=$(extract_id "$LOGIN")

echo "2) Crear transportista"
TRANSPORTISTA=$(api_call POST "$BASE_URL/users" "{\"userName\":\"Luis Transportista ${RUN_ID}\",\"userEmail\":\"${TRANSPORTISTA_EMAIL}\",\"userPassword\":\"trans123\",\"userRole\":\"transportista\"}")
echo "$TRANSPORTISTA"
TRANSPORTISTA_ID=$(extract_id "$TRANSPORTISTA")

echo "3) Crear cliente"
CLIENT=$(api_call POST "$BASE_URL/clients" '{"companyCode":"CLI-002","companyName":"Acme SAC","address":"Lima 123","contactName":"Ana","email":"ana@acme.com","phone":"999888777"}')
echo "$CLIENT"
CLIENT_ID=$(extract_id "$CLIENT")

echo "4) Crear categoria"
CATEGORY=$(api_call POST "$BASE_URL/categories" '{"categoryName":"Fragil"}')
echo "$CATEGORY"
CATEGORY_ID=$(extract_id "$CATEGORY")

echo "5) Crear estado"
STATUS=$(api_call POST "$BASE_URL/statuses" '{"statusName":"En Ruta"}')
echo "$STATUS"
STATUS_ID=$(extract_id "$STATUS")

echo "6) Crear transporte"
TRANSPORT=$(api_call POST "$BASE_URL/transports" '{"transportUserId":null,"transportType":"Camion","transportCapacity":1800,"transportStatus":"available","transportLocation":"Lima","transportDriver":"Carlos","transportLicensePlate":"XYZ-123","transportCompany":"LogiPeru"}')
echo "$TRANSPORT"
TRANSPORT_ID=$(extract_id "$TRANSPORT")

echo "7) Asignar transporte a transportista"
ASSIGNED=$(api_call PATCH "$BASE_URL/transports/$TRANSPORT_ID" "{\"transportUserId\":\"$TRANSPORTISTA_ID\"}")
echo "$ASSIGNED"

echo "8) Crear shipment"
SHIPMENT=$(api_call POST "$BASE_URL/shipments" "{\"shipmentCategoryId\":\"$CATEGORY_ID\",\"shipmentDescription\":\"Medicinas\",\"shipmentPrice\":250.5,\"shipmentWeight\":10,\"shipmentVolume\":2.5,\"shipmentOrigin\":\"Lima\",\"shipmentDestination\":\"Arequipa\",\"shipmentStatusId\":\"$STATUS_ID\",\"shipmentDate\":\"2026-02-20\",\"shipmentClientId\":\"$CLIENT_ID\",\"shipmentTransportId\":\"$TRANSPORT_ID\"}")
echo "$SHIPMENT"
SHIPMENT_ID=$(extract_id "$SHIPMENT")

echo "9) Crear nuevo estado y actualizar shipment"
NEW_STATUS=$(api_call POST "$BASE_URL/statuses" '{"statusName":"Entregado"}')
echo "$NEW_STATUS"
NEW_STATUS_ID=$(extract_id "$NEW_STATUS")
UPDATED=$(api_call PATCH "$BASE_URL/shipments/$SHIPMENT_ID" "{\"shipmentStatusId\":\"$NEW_STATUS_ID\"}")
echo "$UPDATED"

echo "10) Consultas con filtros"
echo "- users role=transportista"
api_call GET "$BASE_URL/users?userRole=transportista"; echo
echo "- transports by user"
api_call GET "$BASE_URL/transports?transportUserId=$TRANSPORTISTA_ID"; echo
echo "- shipments by transport"
api_call GET "$BASE_URL/shipments?shipmentTransportId=$TRANSPORT_ID"; echo

echo "11) Validar evento RabbitMQ consumido en ms-users"
sleep 2
if docker logs daw-ms-users 2>&1 | grep -q "Evento recibido"; then
  echo "OK: ms-users recibio evento shipment.*"
else
  echo "WARN: no se encontro log de evento en ms-users"
fi

echo
printf "Pruebas completadas.\nADMIN_ID=%s\nTRANSPORTISTA_ID=%s\nTRANSPORT_ID=%s\nSHIPMENT_ID=%s\n" \
  "$ADMIN_ID" "$TRANSPORTISTA_ID" "$TRANSPORT_ID" "$SHIPMENT_ID"
