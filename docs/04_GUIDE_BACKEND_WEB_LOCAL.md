# AirGuardNet – Guía de Arranque Local (Backend + Web)

Este documento explica cómo levantar **TODOS los servicios** de AirGuardNet en local y cómo ingresar a la web con usuarios de prueba.

Estado: AirGuardNet v1 (backend + web + BD + datos de prueba)  
NO modificar lógica ni alcance a partir de este punto.

---

## 1. Requisitos

- Java 21
- Node.js LTS (20.x o 22.x)
- PostgreSQL
  - host: `localhost`
  - puerto: `5432`
  - base: `airguardnet`
  - usuario: `postgres`
  - contraseña: `1234`
- Maven 3.x
- (Opcional) Docker para correr PostgreSQL

---

## 2. Base de datos

### 2.1 Crear base de datos

En PostgreSQL:

```sql
CREATE DATABASE airguardnet;

Asegúrate de que el usuario postgres con contraseña 1234 tiene permisos sobre esa BD.

Nota: El esquema y las tablas se crean automáticamente vía JPA (ddl-auto=update) la primera vez que levantas los servicios.

## 3. Backend – Servicios

Ruta raíz backend:

cd backend/airguardnet-backend

3.1 Build completo
mvn clean install


Si todo compila OK, puedes levantar los servicios.

3.2 Levantar microservicios

En terminales separadas:

# api-gateway (8080)
cd backend/airguardnet-backend/api-gateway
mvn spring-boot:run

# user-service (8081)
cd backend/airguardnet-backend/user-service
mvn spring-boot:run

# device-service (8082)
cd backend/airguardnet-backend/device-service
mvn spring-boot:run

# notification-service (8083) – opcional (solo /api/notifications/test)
cd backend/airguardnet-backend/notification-service
mvn spring-boot:run


Backend READY cuando:

Gateway responde en: http://localhost:8080/api

user-service responde vía gateway:

POST /api/login

GET /api/users

device-service responde vía gateway:

GET /api/devices

GET /api/sensor-configs

POST /api/readings/ingest

4. Frontend – Web App

Ruta raíz frontend:

cd web/airguardnet-web


Instalar dependencias:

npm install


Levantar frontend:

npm run dev


Por defecto se abre en:

http://localhost:5173

Rutas principales:

/login

/dashboard

/devices

/alerts

/reports

/users

/config/system

/config/sensors

/config/logs

/docs

/docs/versions

/logs/access

/logs/system

/simulation

5. Usuarios de prueba

Usuarios oficiales cargados en BD:

Gerald Admin – rol ADMIN

Sara Supervisor – rol SUPERVISOR

Tom Técnico – rol TECNICO

Oscar Operador – rol OPERADOR

(Contraseñas: ver seed real o documento interno; si se usan contraseñas estándar, documentarlas aquí.
Ejemplo típico: admin123, super123, tec123, op123 – actualizar este documento con los valores reales.)

Flujo recomendado:

Ir a http://localhost:5173/login.

Ingresar como Gerald Admin (ADMIN).

Ver Dashboard, Dispositivos, Alertas, Configuración, Logs, etc.

Probar también login como Supervisor, Técnico y Operador para validar restricciones por rol.

6. Endpoints clave disponibles vía gateway

Todos se consumen en frontend a través de:

http://localhost:8080/api

Algunos endpoints principales:

Auth / Users:

POST /api/login

GET /api/users

GET /api/users/plans

GET /api/users/plans/{planId}/features

Dispositivos / Lecturas:

GET /api/devices

GET /api/devices/{id}

GET /api/devices/{id}/readings

GET /api/devices/{id}/alerts

POST /api/readings/ingest

Config / Sensores:

GET /api/config-parameters

PUT /api/config-parameters/{key}

GET /api/sensor-configs

PUT /api/sensor-configs/{id}

Logs:

GET /api/access-logs

GET /api/system-logs

Reportes:

GET /api/usage-reports

7. Notas para pruebas futuras (Chat 3)

El sistema ya contiene:

Lecturas históricas (PM1, PM2.5, PM10).

Alertas en múltiples estados (PENDING, IN_PROGRESS, RESOLVED).

Incidencias (OPEN, IN_PROGRESS, CLOSED).

Logs de acceso y sistema.

Notas de sensor.

Esto permite:

Pruebas manuales completas pantalla por pantalla.

Pruebas automáticas (unitarias, integración, Selenium) sin tener que sembrar datos cada vez.

Regla de oro:
Desde este punto, no modificar lógica de negocio en backend ni comportamiento de pantallas sin actualizar antes el Documento Maestro y la Especificación oficial.


────────────────────────
2. Optional pero MUY útil: docs/05_CHECKLIST_FLUJOS_WEB.md
────────────────────────

Esto ya es pensando en Chat 3 (pruebas), pero lo dejamos armado desde Chat 1 para no perder el contexto.

Crea `docs/05_CHECKLIST_FLUJOS_WEB.md`:

```markdown
# AirGuardNet – Checklist de Flujos Web (para pruebas)

Este checklist resume los flujos principales que deben funcionar SIEMPRE en AirGuardNet.

## 1. Login y roles

[ ] Login ADMIN → acceso a todas las secciones  
[ ] Login SUPERVISOR → acceso a dashboard, dispositivos, alertas, reportes, logs (según configuración actual)  
[ ] Login TECNICO → acceso a dispositivos, alertas, incidencias (según configuración)  
[ ] Login OPERADOR → acceso limitado (solo su vista necesaria, según implementación final)  
[ ] Mensaje claro en caso de credenciales inválidas  
[ ] Logout limpia token y redirige a /login  

## 2. Dashboard

[ ] KPIs muestran valores coherentes con BD (`total_devices`, `total_readings`, `total_alerts`, etc.)  
[ ] Gráfico de PM2.5 carga sin errores (o muestra mensaje “sin datos” si aplica)  
[ ] GlobalRiskIndex refleja razonablemente el estado de las últimas lecturas  

## 3. Dispositivos

[ ] Lista de dispositivos muestra todos los AG-ESP32-000x  
[ ] Filtros por estado (ACTIVE, WARNING, CRITICAL, OFFLINE) funcionan  
[ ] Detalle de dispositivo muestra lecturas recientes y alertas asociadas  
[ ] Gráfico por dispositivo refleja lecturas históricas coherentes  

## 4. Alertas

[ ] Centro de alertas lista CRITICAL / HIGH / MEDIUM con sus estados  
[ ] Filtros por severidad y estado funcionan  
[ ] Cambio de estado (PENDING → IN_PROGRESS → RESOLVED) se refleja en BD  
[ ] Detalle de alerta muestra lectura asociada y dispositivo  

## 5. Configuración

[ ] `config-parameters` se listan y se pueden editar (ej. DEVICE_OFFLINE_MINUTES)  
[ ] Cambios generan entradas en `config_change_logs`  
[ ] `sensor-configs` (PM1, PM25, PM10) se listan y actualizan correctamente  

## 6. Usuarios

[ ] Tabla de usuarios muestra todos los usuarios de prueba  
[ ] Crear usuario nuevo funciona (si está habilitado)  
[ ] Cambiar status (ACTIVE / DISABLED) funciona  
[ ] Ver / editar rol y plan se refleja correctamente  

## 7. Logs

[ ] `access-logs` muestra sesiones de login/logout  
[ ] `system-logs` muestra info, warnings y errores (incluyendo lecturas extremas)  

## 8. Reportes

[ ] `usage-reports` se muestran sin errores (aunque sean pocos registros)  
[ ] Gráficos de distribución de alertas reflejan los datos actuales  

## 9. Simulación

[ ] Botón “Normal” genera lecturas simuladas bajas (verde)  
[ ] Botón “Pico de polvo” genera lecturas simuladas altas (amarillo/rojo)  
[ ] Botón “Volver a normalidad” hace que la gráfica vuelva a valores seguros  
[ ] NO realiza llamadas reales al backend  

## 10. Regresión rápida

Antes de entregar:

[ ] Probar rápido login ADMIN y navegación por todas las secciones  
[ ] Verificar que ninguna pantalla lanza error en consola/pantalla  
[ ] Confirmar que el backend no lanza excepciones inesperadas en logs  