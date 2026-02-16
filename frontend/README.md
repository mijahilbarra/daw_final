# Logistics SPA

SPA en Angular + TypeScript, con arquitectura simple para usar Firestore hoy y migrar a backend HTTP despues.

## Estructura

- `src/app/features/*`: una carpeta por clase de dominio (`users`, `transports`, `clients`, `categories`, `statuses`, `shipments`) con `model.ts` y `feature.ts`.
- `src/app/repo/pto/repository-pto.ts`: puerto (PTO) de repositorio.
- `src/app/repo/firestore/*`: implementacion con Firestore (`onSnapshot` para realtime).
- `src/app/repo/http/*`: implementacion HTTP (polling simple).
- `src/app/repo/repository.factory.ts`: selector de implementacion segun modo.

## Flujo funcional

- Login con Firebase Auth (`email/password`) + perfil en `users`.
- Si el usuario autentica y no tiene perfil, se crea automaticamente en `users` con rol inicial `transportista`.
- Si `userRole = admin`:
  - crear `transport`, `client`, `category`, `status`, `shipment`.
  - crear `transportista` (Auth + perfil en Firestore).
  - asignar `transport` a `transportista`.
- Si `userRole = transportista`:
  - ver transportes asignados.
  - ver shipments asignados.
  - cambiar estado de shipment.

## Configurar Firebase

Editar:

- `src/app/firebase/firebase.config.ts`

Reemplazar `REPLACE_ME` con tus credenciales de Firebase.

## Estructura de `users` en Firestore

Cada usuario debe tener un perfil en la coleccion `users` con:

- `authUid` (uid de Firebase Auth)
- `userName`
- `userEmail`
- `userRole` (`admin` o `transportista`)

Valores validos para `userRole`:

- `admin`
- `transportista`

## Ejecutar

```bash
npm install
npm start
```

## Migrar de Firestore a backend HTTP

1. Cambiar en `src/app/repo/data-source.config.ts`:
   - `DATA_SOURCE_MODE` de `'firestore'` a `'http'`.
2. Ajustar `API_BASE_URL` a tu API real.
3. Mantener `features` y componentes sin cambios.

## Nota de realtime

- En modo Firestore se usa `onSnapshot` (tipo websocket realtime).
- En modo HTTP se usa polling cada 5 segundos para mantener compatibilidad simple.
