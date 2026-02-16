# Endpoints Backend

## Base URL
- API Gateway: `http://localhost:8080/api`
- Health: `http://localhost:8080/actuator/health`

## Convenciones de params
- Los `GET` con filtros usan `@RequestParam Map<String, String>`.
- Puedes enviar cualquier query param cuyo nombre coincida con un campo del modelo (`id` incluido).
- El filtro es por igualdad exacta (`String.valueOf(field).equals(valor)`), sin operaciones como `like`, rangos o paginación.

## Users (`/api/users`)

### `GET /api/users`
- Path params: ninguno
- Query params posibles: `id`, `userName`, `userEmail`, `userPassword`, `userRole`
- Body: no aplica

### `POST /api/users`
- Path params: ninguno
- Query params: ninguno
- Body (`application/json`):
```json
{
  "id": "string (opcional)",
  "userName": "string",
  "userEmail": "string",
  "userPassword": "string",
  "userRole": "string (opcional, default: transportista)"
}
```

### `PATCH /api/users/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body parcial (`application/json`) - campos soportados:
```json
{
  "userName": "string",
  "userEmail": "string",
  "userPassword": "string",
  "userRole": "string"
}
```

### `DELETE /api/users/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body: no aplica

### `POST /api/users/login`
- Path params: ninguno
- Query params: ninguno
- Body (`application/json`):
```json
{
  "email": "string",
  "password": "string"
}
```

## Clients (`/api/clients`)

### `GET /api/clients`
- Path params: ninguno
- Query params posibles: `id`, `companyCode`, `companyName`, `address`, `contactName`, `email`, `phone`
- Body: no aplica

### `POST /api/clients`
- Path params: ninguno
- Query params: ninguno
- Body (`application/json`):
```json
{
  "id": "string (opcional)",
  "companyCode": "string",
  "companyName": "string",
  "address": "string",
  "contactName": "string",
  "email": "string",
  "phone": "string"
}
```

### `PATCH /api/clients/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body parcial (`application/json`) - campos soportados:
```json
{
  "companyCode": "string",
  "companyName": "string",
  "address": "string",
  "contactName": "string",
  "email": "string",
  "phone": "string"
}
```

### `DELETE /api/clients/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body: no aplica

## Categories (`/api/categories`)

### `GET /api/categories`
- Path params: ninguno
- Query params posibles: `id`, `categoryName`
- Body: no aplica

### `POST /api/categories`
- Path params: ninguno
- Query params: ninguno
- Body (`application/json`):
```json
{
  "id": "string (opcional)",
  "categoryName": "string"
}
```

### `PATCH /api/categories/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body parcial (`application/json`) - campos soportados:
```json
{
  "categoryName": "string"
}
```

### `DELETE /api/categories/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body: no aplica

## Statuses (`/api/statuses`)

### `GET /api/statuses`
- Path params: ninguno
- Query params posibles: `id`, `statusName`
- Body: no aplica

### `POST /api/statuses`
- Path params: ninguno
- Query params: ninguno
- Body (`application/json`):
```json
{
  "id": "string (opcional)",
  "statusName": "string"
}
```

### `PATCH /api/statuses/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body parcial (`application/json`) - campos soportados:
```json
{
  "statusName": "string"
}
```

### `DELETE /api/statuses/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body: no aplica

## Transports (`/api/transports`)

### `GET /api/transports`
- Path params: ninguno
- Query params posibles: `id`, `transportUserId`, `transportType`, `transportCapacity`, `transportStatus`, `transportLocation`, `transportDriver`, `transportLicensePlate`, `transportCompany`
- Body: no aplica

### `POST /api/transports`
- Path params: ninguno
- Query params: ninguno
- Body (`application/json`):
```json
{
  "id": "string (opcional)",
  "transportUserId": "string | null",
  "transportType": "string",
  "transportCapacity": "number",
  "transportStatus": "string (opcional, default: available)",
  "transportLocation": "string",
  "transportDriver": "string",
  "transportLicensePlate": "string",
  "transportCompany": "string"
}
```

### `PATCH /api/transports/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body parcial (`application/json`) - campos soportados:
```json
{
  "transportUserId": "string | null",
  "transportType": "string",
  "transportCapacity": "number",
  "transportStatus": "string",
  "transportLocation": "string",
  "transportDriver": "string",
  "transportLicensePlate": "string",
  "transportCompany": "string"
}
```

### `DELETE /api/transports/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body: no aplica

## Shipments (`/api/shipments`)

### `GET /api/shipments`
- Path params: ninguno
- Query params posibles: `id`, `shipmentCategoryId`, `shipmentDescription`, `shipmentPrice`, `shipmentWeight`, `shipmentVolume`, `shipmentOrigin`, `shipmentDestination`, `shipmentStatusId`, `shipmentDate`, `shipmentClientId`, `shipmentTransportId`
- Body: no aplica

### `POST /api/shipments`
- Path params: ninguno
- Query params: ninguno
- Body (`application/json`):
```json
{
  "id": "string (opcional)",
  "shipmentCategoryId": "string",
  "shipmentDescription": "string",
  "shipmentPrice": "number",
  "shipmentWeight": "number",
  "shipmentVolume": "number",
  "shipmentOrigin": "string",
  "shipmentDestination": "string",
  "shipmentStatusId": "string",
  "shipmentDate": "string (YYYY-MM-DD)",
  "shipmentClientId": "string",
  "shipmentTransportId": "string"
}
```

### `PATCH /api/shipments/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body parcial (`application/json`) - campos soportados:
```json
{
  "shipmentCategoryId": "string | null",
  "shipmentDescription": "string | null",
  "shipmentPrice": "number | null",
  "shipmentWeight": "number | null",
  "shipmentVolume": "number | null",
  "shipmentOrigin": "string | null",
  "shipmentDestination": "string | null",
  "shipmentStatusId": "string | null",
  "shipmentDate": "string YYYY-MM-DD",
  "shipmentClientId": "string | null",
  "shipmentTransportId": "string | null"
}
```

### `DELETE /api/shipments/{id}`
- Path params:
  - `id` (string)
- Query params: ninguno
- Body: no aplica
