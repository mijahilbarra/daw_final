# DAW Backend (Microservicios)

Stack MVP:
- Eureka Server
- API Gateway
- OpenFeign
- RabbitMQ
- PostgreSQL (DB por servicio)

Servicios:
- `ms-users` (users + login)
- `ms-masterdata` (clients, categories, statuses)
- `ms-logistics` (transports, shipments)

## Ejecutar
```bash
./up.sh
./test-endpoints.sh
./down.sh
```

## Credenciales seed
- admin: `admin@daw.com` / `admin123`
- transportista: `transportista@daw.com` / `trans123`

## Gateway
- Base URL: `http://localhost:8080/api`
- Health: `http://localhost:8080/actuator/health`
