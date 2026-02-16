# DAW - Arquitectura y Uso de Tecnologias

Este proyecto implementa una solucion de microservicios para gestion de envios, con frontend Angular y backend Spring Boot.

## 1. Tecnologias usadas y en que archivos

### Eureka (Service Discovery)
- Servidor Eureka:
  - `backend/eureka-server/src/main/java/com/cibertec/eureka/EurekaServerApplication.java`
  - `backend/eureka-server/src/main/resources/application.yml`
- Clientes Eureka (registro de microservicios):
  - `backend/ms-users/src/main/resources/application.yml`
  - `backend/ms-masterdata/src/main/resources/application.yml`
  - `backend/ms-logistics/src/main/resources/application.yml`
  - `backend/api-gateway/src/main/resources/application.yml`

### API Gateway (entrada unica)
- App gateway:
  - `backend/api-gateway/src/main/java/com/cibertec/gateway/ApiGatewayApplication.java`
- Rutas y CORS:
  - `backend/api-gateway/src/main/resources/application.yml`
- Rutas principales:
  - `/api/users/**` -> `ms-users`
  - `/api/clients/**`, `/api/categories/**`, `/api/statuses/**` -> `ms-masterdata`
  - `/api/transports/**`, `/api/shipments/**` -> `ms-logistics`

### Feign (comunicacion sincrona entre microservicios)
- Feign habilitado:
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/MsLogisticsApplication.java`
- Clientes Feign:
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/client/UsersClient.java`
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/client/MasterdataClient.java`
- Uso en logica de negocio:
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/service/TransportService.java`
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/service/ShipmentService.java`

### RabbitMQ (comunicacion asincrona por eventos)
- Configuracion de exchange:
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/config/RabbitConfig.java`
  - `backend/ms-users/src/main/java/com/cibertec/users/config/RabbitConfig.java`
- Publicacion de eventos:
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/messaging/ShipmentEventPublisher.java`
- Consumo de eventos:
  - `backend/ms-users/src/main/java/com/cibertec/users/messaging/ShipmentEventConsumer.java`
- Configuracion en Docker:
  - `backend/docker-compose.yml` (servicio `rabbitmq`)

### Docker / Docker Compose
- Orquestacion completa:
  - `backend/docker-compose.yml`
- Dockerfiles por servicio:
  - `backend/eureka-server/Dockerfile`
  - `backend/api-gateway/Dockerfile`
  - `backend/ms-users/Dockerfile`
  - `backend/ms-masterdata/Dockerfile`
  - `backend/ms-logistics/Dockerfile`
- Scripts de arranque/prueba/parada:
  - `up.sh`, `test-endpoints.sh`, `down.sh` (raiz)
  - `backend/up.sh`, `backend/test-endpoints.sh`, `backend/down.sh`

### PostgreSQL (DB por microservicio)
- Definido en:
  - `backend/docker-compose.yml` (`postgres-users`, `postgres-masterdata`, `postgres-logistics`)
- Configuracion datasource:
  - `backend/ms-users/src/main/resources/application.yml`
  - `backend/ms-masterdata/src/main/resources/application.yml`
  - `backend/ms-logistics/src/main/resources/application.yml`

### Spring Boot + JPA (microservicios)
- Entidades y repositorios:
  - `backend/ms-users/src/main/java/com/cibertec/users/entity/UserEntity.java`
  - `backend/ms-masterdata/src/main/java/com/cibertec/masterdata/entity/*`
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/entity/*`
- Controladores REST:
  - `backend/ms-users/src/main/java/com/cibertec/users/controller/UserController.java`
  - `backend/ms-masterdata/src/main/java/com/cibertec/masterdata/controller/*`
  - `backend/ms-logistics/src/main/java/com/cibertec/logistics/controller/*`

### Angular (frontend)
- App principal:
  - `frontend/src/app/app.ts`
  - `frontend/src/app/app.html`
- Pantallas:
  - `frontend/src/app/pages/admin/admin-page.ts`
  - `frontend/src/app/pages/transportista/transportista-page.ts`
- Capa de acceso a datos HTTP:
  - `frontend/src/app/repo/http/http.repository.ts`
  - `frontend/src/app/repo/repository.factory.ts`
  - `frontend/src/app/repo/data-source.config.ts`
- Features por modulo:
  - `frontend/src/app/features/users/user.feature.ts`
  - `frontend/src/app/features/transports/transport.feature.ts`
  - `frontend/src/app/features/clients/client.feature.ts`
  - `frontend/src/app/features/categories/category.feature.ts`
  - `frontend/src/app/features/statuses/status.feature.ts`
  - `frontend/src/app/features/shipments/shipment.feature.ts`

## 2. Flujo sincrono principal
- Admin crea/actualiza datos desde frontend.
- Frontend consume Gateway (`http://localhost:8080/api`).
- Gateway enruta al microservicio correspondiente.
- `ms-logistics` valida referencias con Feign antes de guardar (usuario, cliente, categoria, estado, transporte).

## 3. Flujo asincrono principal
- Al crear shipment o cambiar estado, `ms-logistics` publica evento en RabbitMQ.
- `ms-users` consume el evento para trazabilidad (log de recepcion).

## 4. Endpoints principales (via gateway)
- `POST /api/users/login`
- `GET/POST/PATCH/DELETE /api/users`
- `GET/POST/PATCH/DELETE /api/clients`
- `GET/POST/PATCH/DELETE /api/categories`
- `GET/POST/PATCH/DELETE /api/statuses`
- `GET/POST/PATCH/DELETE /api/transports`
- `GET/POST/PATCH/DELETE /api/shipments`

## 5. Ejecucion
Desde la raiz `DAW`:

```bash
./up.sh
./test-endpoints.sh
./down.sh
```

Credenciales seed:
- admin: `admin@daw.com` / `admin123`
- transportista: `transportista@daw.com` / `trans123`

## 6. Diagramas
- Diagrama de clases: `ClassDiagram.md`
- Diagrama entidad-relacion: `EntityDiagram.md`
