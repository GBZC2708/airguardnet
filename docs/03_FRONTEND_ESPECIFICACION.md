```markdown
# AirGuardNet – Especificación Frontend Web

Este documento define:

- La estructura de módulos y pantallas del frontend web.
- Los flujos de navegación.
- Qué endpoints del backend consume cada pantalla.
- Las reglas de diseño (tema oscuro, colores, tipografía).
- Reglas de seguridad y manejo de JWT en el cliente.

## 1. Stack y estructura

Stack:

- Vite + React + TypeScript
- MUI (Material UI)
- React Router DOM
- Axios

Estructura base ya creada en `web/airguardnet-web/src`:

- core/
  - api/
  - layout/
  - routing/
  - theme/
  - types/
- modules/
  - auth/
  - dashboard/
  - devices/
  - alerts/
  - reports/
  - users/
  - config/
  - docs/
  - logs/
  - simulation/

## 2. Tema, diseño y UX

Tema oscuro:

- Fondo principal: azul muy oscuro.
- Cards: ligeramente más claras.
- Acentos: cian/neón para elementos interactivos.
- Estados:
  - Seguro: verde.
  - Advertencia: amarillo.
  - Crítico: rojo.

Tipografía:

- Inter o Poppins como fuente principal.

Layout:

- AuthLayout:
  - Formulario centrado, card sobre fondo oscuro.
- MainLayout:
  - Sidebar con navegación principal.
  - Topbar con título, usuario y logout.
  - Contenido central con cards y gráficos.

Responsivo:

- Desktop:
  - Dashboard en múltiples columnas.
- Tablet:
  - 2 columnas.
- Móvil:
  - Cards apiladas verticalmente.

## 3. API client y auth en frontend

### 3.1 httpClient.ts

- Configurar axios con:
  - baseURL: http://localhost:8080/api
  - Interceptor de request:
    - Si existe token JWT en contexto/localStorage, agregar header Authorization: Bearer <token>.
  - Interceptor de respuesta:
    - Si status 401:
      - Limpiar sesión y redirigir a /login.

### 3.2 AuthContext y ProtectedRoute

- AuthContext:
  - user (User | null).
  - token (string | null).
  - login(credentials).
  - logout().
  - register(data) si aplica.

- Login:
  - POST /api/login con { email, password }.
  - Guardar token en localStorage.
  - Obtener datos de usuario (via /api/users/me si existe, o decodificando token).
  - Redirigir a /dashboard.

- ProtectedRoute:
  - Si no hay token o user:
    - Redirigir a /login.
  - Si sí:
    - Renderizar children.

## 4. Módulos y pantallas

### 4.1 Auth

Rutas:

- /login
- /register (si se permite)
- /forgot-password (UI, lógica de backend según HU)

Pantallas:

- LoginPage:
  - Form:
    - email
    - password
  - Botón Iniciar sesión:
    - Llama a authApi.login.
    - Maneja errores con mensajes visibles.

- RegisterPage:
  - Según política (para este documento, dejar TODO si no se define claramente).

### 4.2 Dashboard

Ruta:

- / o /dashboard

Componentes:

- SummaryCards:
  - Muestra:
    - total_devices
    - total_readings (de hoy)
    - critical_alerts_open
    - average_air_quality_percent (hoy)
  - Origen:
    - /api/usage-reports (último) y/o endpoints estadísticos.

- ChartsSection:
  - Gráfico 24h PM2.5:
    - Llama a /api/devices/{id}/readings o a un endpoint agregado de estadísticas.
  - Distribución de alertas:
    - Llama a /api/alerts con filtro de rango de fecha.

- GlobalRiskIndex:
  - Calcula un índice global a partir de risk_index promedio de lecturas recientes.

### 4.3 Devices

Rutas:

- /devices
- /devices/:id

DevicesListPage:

- Tabla o cards con:
  - name
  - device_uid
  - status
  - last_communication_at
  - last_battery_level
  - último PM2.5
- Filtros:
  - Por status (ACTIVE, WARNING, CRITICAL, OFFLINE).
- Acciones:
  - Ver detalle (navegar a /devices/:id).

DeviceDetailPage:

- Datos del dispositivo.
- Gráfico de PM2.5 en rango de fechas.
- Lista de lecturas recientes.
- Lista de alertas asociadas.
- Configuración de sensores (tabla sensor_configs).
- Notas de sensor (sensor_notes).

Endpoints:

- GET /api/devices
- GET /api/devices/{id}
- GET /api/devices/{id}/readings
- GET /api/devices/{id}/alerts
- GET /api/sensor-configs (para PM1, PM25, PM10)
- GET/POST /api/sensor-notes (si se expone, o se puede dejar TODO si no está definido)

### 4.4 Alerts

Ruta:

- /alerts

AlertsCenterPage:

- Tabla:
  - device_name
  - severity
  - status
  - created_at
  - mensaje
- Filtros:
  - severity (CRITICAL, HIGH, MEDIUM).
  - status (PENDING, IN_PROGRESS, RESOLVED).
  - rango de fechas.
- Acciones:
  - Cambiar status.
  - Ver detalle.

Endpoints:

- GET /api/alerts
- PATCH /api/alerts/{id}/status

### 4.5 Reports

Ruta:

- /reports

ReportsPage:

- KpiCards:
  - total_users
  - total_devices
  - total_readings
  - total_alerts
- TimeSeriesCharts:
  - PM2.5 vs tiempo (rango de fechas).
- AlertDistributionCharts:
  - Número de alertas por nivel y por día.

Endpoints:

- GET /api/usage-reports
- GET /api/readings/stats (si se define uno; si no, usar /api/devices/{id}/readings para un device principal y dejar TODO para consolidar estadístico global).

### 4.6 Users

Ruta:

- /users

UsersManagementPage:

- Tabla de usuarios:
  - name
  - last_name
  - email
  - role
  - plan
  - status
  - last_login_at
- Acciones:
  - Crear usuario.
  - Editar usuario.
  - Cambiar status.

Endpoints:

- GET /api/users
- POST /api/users
- GET /api/plans
- PATCH /api/users/{id}/status

### 4.7 Config

Rutas:

- /config/system
- /config/sensors
- /config/logs

SystemConfigPage:

- Formulario de config_parameters:
  - Ejemplo:
    - SYSTEM_NAME
    - DEFAULT_TIMEZONE
    - GLOBAL_PM25_CRITICAL

SensorThresholdsPage:

- Tabla sensor_configs para PM1, PM25, PM10.
- Permite editar recommended_max y critical_threshold.

ConfigChangeLogPage:

- Tabla config_change_logs:
  - parameter_key
  - old_value
  - new_value
  - changed_by
  - changed_at

Endpoints:

- GET /api/config-parameters
- PUT /api/config-parameters/{key}
- GET /api/sensor-configs
- PUT /api/sensor-configs/{id}
- GET /api/config-change-logs

### 4.8 Docs

Rutas:

- /docs
- /docs/versions

DocumentationPage:

- Texto estático / contenido de ayuda.

VersionHistoryPage:

- Tabla version_history.

Endpoints:

- GET /api/version-history

### 4.9 Logs

Rutas:

- /logs/access
- /logs/system

AccessLogsPage:

- Tabla access_logs:
  - user
  - action
  - ip_address
  - created_at

SystemLogsPage:

- Tabla system_logs:
  - type
  - source
  - message
  - created_at

Endpoints:

- GET /api/access-logs
- GET /api/system-logs

### 4.10 Simulation

Ruta:

- /simulation

SimulationPage:

- SimulationControls:
  - Botones:
    - Normal
    - Pico de polvo
    - Volver a normalidad
- SimulationLiveChart:
  - Gráfico en tiempo real de datos simulados:
    - PM2.5
    - risk_index
    - air_quality_percent

Importante:

- La simulación se implementa solo en frontend con datos generados en memoria.
- No agregar endpoints nuevos para simulación.
- No introducir nuevos sensores.

## 5. Reglas generales para la implementación frontend

- No mover ni renombrar carpetas base ya creadas.
- No agregar módulos fuera de lo definido.
- Consumir exclusivamente los endpoints documentados en 02_BACKEND_ESPECIFICACION.md.
- Manejo de errores:
  - Mostrar mensajes claros en UI cuando una llamada falle.
  - Si es 401:
    - Redirigir a login.
- Mantener coherencia visual:
  - Misma paleta, tipografía y estilo de cards/gráficos en todas las pantallas.