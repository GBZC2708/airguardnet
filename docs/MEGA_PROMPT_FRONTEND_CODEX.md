
MEGA_PROMPT_FRONTEND_CODEX.md
────────────────────────

# MEGA PROMPT FRONTEND – AirGuardNet (para Codex)

Eres una IA de desarrollo que va a trabajar SOLO en el frontend web de AirGuardNet.

Este archivo es tu CONTRATO.
Debes leerlo TODO y cumplirlo al 100%.

NO debes tocar:

backend/airguardnet-backend

archivos de configuración Maven o Spring

documentos de especificación (docs/*.md)

Tu trabajo es completar TODO el frontend web en:

web/airguardnet-web/

respetando SIEMPRE:

El Documento Maestro de AirGuardNet (solo polvo con PMS5003).

02_BACKEND_ESPECIFICACION.md (modelo de datos y endpoints).

03_FRONTEND_ESPECIFICACION.md (módulos/pantallas).

El estado actual del backend (endpoints reales y wrapper ApiResponse).

Las 10 imágenes adjuntas como REFERENCIA VISUAL de diseño (no de funcionalidad extra).

0. Referencia visual (las 10 imágenes)

Se te entregan 10 capturas de una UI estilo AirGuardNet. Son SOLO referencia de estilo:

Dashboard con:

Card superior de “ubicación actual”.

Lista de “Alertas activas” con cards grandes, bordeado por color según severidad.

Sidebar izquierda con avatar, rol, menú y footer con “Sincronizado / Cerrar sesión”.

“Mi Dispositivo”:

Card grande de usuario + dispositivo asignado.

Card de batería con barra horizontal (porcentaje).

Cards de lecturas de sensores.

“Historial de Alertas”:

KPIs de total, críticas, pendientes, resueltas.

Chips de filtro (Todas, Críticas, etc.).

Lista de alertas con cards coloreadas por severidad.

“Mis Tickets”:

Lista de tickets con estado.

KPIs arriba.

Botón “Nuevo Ticket”.

“Chat en Vivo”:

Panel de chat.

Tarjetas de preguntas frecuentes.

“Centro de Documentación”:

Buscador.

Lista de categorías a la izquierda.

Bloque grande de bienvenida.

Cards de “Video Tutoriales”.

“Perfil de Usuario”:

Card con avatar grande, nombre y plan.

Pestañas: Información Personal, Empresa, Seguridad, Cuenta, Notificaciones.

“Panel de Notificaciones”:

Dropdown de notificaciones con lista de alertas.

Pantalla de Login:

Columna izquierda: login + selección de tipo de usuario.

Columna derecha: “Tecnología de Monitoreo Avanzado” + KPIs.

Diálogo de logout:

Modal centrado “¿Estás seguro que deseas salir?”.

DEBES copiar al máximo el LOOK & FEEL (tema, colores, disposición, cards, chips, sombras) pero ADAPTADO a nuestro alcance:

Nada de gases (H2S, CO, O2, etc.).

Sólo polvo: PM1, PM2.5, PM10 y battery_level.

Las pantallas “Mis Tickets” y “Chat en Vivo” NO SON módulos a implementar, sólo referencia de estilo para:

Alertas, Logs, Docs y sección de soporte estático.

1. Alcance fijo del proyecto (recordatorio duro)

Proyecto: AirGuardNet.

Enfoque:

Monitoreo de polvo en minería usando SOLO sensor PMS5003.

Magnitudes:

PM1

PM2.5

PM10

battery_level (0–100 %)

El frontend NO debe:

Agregar sensores nuevos (NO H2S, NO CO, NO vibraciones, NO temperatura, etc.).

Agregar módulos fuera de:

auth

dashboard

devices

alerts

reports

users

config

docs

logs

simulation

Texto en UI:

Donde las imágenes digan “gases tóxicos”, “H2S crítico”, etc., tú debes escribir:

“Polvo elevado”, “Nivel de polvo crítico”, “PM2.5 supera umbral seguro”, etc.

2. Estado REAL del backend (lo que DEBES asumir)

Base URL ÚNICA para el frontend:

http://localhost:8080/api

Puertos (solo informativo):

api-gateway → 8080

user-service → 8081

device-service → 8082

notification-service → 8083

BD:

PostgreSQL airguardnet (localhost:5432, usuario postgres, pass 1234).

Seguridad:

user-service genera JWT en /api/login.

api-gateway actualmente NO valida JWT, solo enruta, pero el frontend debe manejar token igual (pensando en futuro).

Wrapper de respuesta (OBLIGATORIO):

Todas las respuestas REST tienen forma:

{
  "success": boolean,
  "message": "texto o null",
  "data": <payload real>
}


NUNCA leas los datos directo de la raíz de la respuesta. Siempre de data.

Ejemplo de login exitoso:

{
  "success": true,
  "message": null,
  "data": {
    "token": "JWT_HERE",
    "userId": 1,
    "name": "Admin",
    "email": "admin@demo.com",
    "role": "ADMIN",
    "planId": 1
  }
}


Ejemplo de error:

{
  "success": false,
  "message": "Credenciales inválidas",
  "data": null
}


El backend “v1” está 100% funcional y, según la especificación, expone al menos estos endpoints (úsalos, NO inventes otros):

AUTH / USERS:

POST /api/register

POST /api/login

GET /api/users

GET /api/users/{id}

PATCH /api/users/{id}/status

GET /api/users/plans

GET /api/users/plans/{planId}/features

CONFIG:

GET /api/config-parameters

PUT /api/config-parameters/{key}

GET /api/sensor-configs

PUT /api/sensor-configs/{id}

GET /api/config-change-logs

DISPOSITIVOS / LECTURAS / ALERTAS:

GET /api/devices

GET /api/devices/{id}

GET /api/devices/{id}/readings

GET /api/devices/{id}/alerts

POST /api/readings/ingest (pruebas técnicas)

GET /api/alerts

PATCH /api/alerts/{id}/status

POST /api/incidents

PATCH /api/incidents/{id}

REPORTES:

GET /api/usage-reports

LOGS:

GET /api/access-logs

GET /api/system-logs

DOCS / VERSIONES:

GET /api/version-history

NOTIFICATIONS:

GET /api/notifications/test (solo ping de prueba; úsalo si quieres mostrar “servicio de notificaciones activo”).

Si algún endpoint exacto no está implementado, NO inventes otro nombre: deja un // TODO con la referencia a la especificación. Pero el objetivo es que, con el estado actual, TODO sea funcional.

3. Estructura del frontend (YA creada, NO renombrar)

Repositorio frontend:

web/airguardnet-web/

Stack:

Vite + React + TypeScript

MUI (Material UI)

React Router DOM

Axios

Estructura existente en src/ (NO cambiar nombres ni mover):

src/
  main.tsx
  App.tsx
  core/
    api/
      httpClient.ts
      authApi.ts
      deviceApi.ts
      alertApi.ts
      reportApi.ts
      configApi.ts
    layout/
      MainLayout.tsx
      AuthLayout.tsx
    routing/
      routes.tsx
      ProtectedRoute.tsx
    theme/
      theme.ts
    types/
      (interfaces TS)
  modules/
    auth/
      pages/
        LoginPage.tsx
        RegisterPage.tsx
        ForgotPasswordPage.tsx
      components/
        LoginForm.tsx
      hooks/
        useAuth.ts
      context/
        AuthContext.tsx
    dashboard/
      pages/
        DashboardPage.tsx
      components/
        SummaryCards.tsx
        ChartsSection.tsx
        GlobalRiskIndex.tsx
    devices/
      pages/
        DevicesListPage.tsx
        DeviceDetailPage.tsx
      components/
        DeviceCard.tsx
        DeviceFilters.tsx
    alerts/
      pages/
        AlertsCenterPage.tsx
      components/
        AlertsTable.tsx
        AlertFilters.tsx
        AlertDetailDrawer.tsx
    reports/
      pages/
        ReportsPage.tsx
      components/
        KpiCards.tsx
        TimeSeriesCharts.tsx
        AlertDistributionCharts.tsx
    users/
      pages/
        UsersManagementPage.tsx
      components/
        UsersTable.tsx
        UserFormDialog.tsx
    config/
      pages/
        SystemConfigPage.tsx
        SensorThresholdsPage.tsx
        ConfigChangeLogPage.tsx
      components/
        ConfigForm.tsx
        SensorConfigTable.tsx
        ChangeLogTable.tsx
    docs/
      pages/
        DocumentationPage.tsx
        VersionHistoryPage.tsx
    logs/
      pages/
        SystemLogsPage.tsx
        AccessLogsPage.tsx
      components/
        LogsTable.tsx
    simulation/
      pages/
        SimulationPage.tsx
      components/
        SimulationControls.tsx
        SimulationLiveChart.tsx


Debes COMPLETAR esta estructura, NO renombrarla ni mover carpetas.

Puedes agregar más componentes dentro de carpetas existentes, por ejemplo:

src/core/components/Sidebar.tsx

src/core/components/Topbar.tsx

etc.

4. Diseño, tema y UX (copiar estilo de las imágenes)
4.1. Theme global (core/theme/theme.ts)

Configura un tema oscuro similar a las capturas:

palette.background.default: azul muy oscuro.

palette.background.paper: azul algo más claro.

palette.primary.main: cian/neón (botones, elementos activos).

palette.secondary.main: otro tono azul/cian.

palette.success.main: verde (estado seguro).

palette.warning.main: amarillo (advertencia).

palette.error.main: rojo (crítico).

Textos secundarios en gris claro.

Tipografía:

Usa Inter o Poppins.

Títulos (h4, h5, h6) más grandes y en negrita.

Cuerpo regular.

Componentes globales:

Botones contained y outlined con bordes muy redondeados.

Cards con border-radius grande y sombras suaves.

Chips de estado (Seguro, Advertencia, Crítico) con colores de paleta.

4.2. Layouts inspirados en las imágenes

AuthLayout:

Pantalla dividida en dos columnas:

Izquierda: formulario de login.

Derecha: panel de marketing.

Izquierda:

Logo AirGuardNet.

Título “Acceso al Sistema”.

Tres tarjetas “Tipo de Usuario”: Administrador, Supervisor, Operador.

Estas tarjetas solo afectan el ESTILO visual (no la lógica de login).

Form con email, password, checkbox “Recordarme”, link “¿Olvidaste tu contraseña?”, botón “Iniciar Sesión”.

Derecha:

Icono grande tipo tarjeta brillante.

Título “Tecnología de Monitoreo de Polvo Avanzado”.

Subtítulo adaptado a polvo, no gases.

Tres KPI cards (“Monitoreo 24/7”, “Precisión 99.9%”, “+150 operaciones mineras confían”, etc.).

MainLayout:

Sidebar fija a la izquierda:

Logo en la parte superior.

Card con usuario actual (iniciales, nombre, badge de rol).

Menú de navegación vertical:

Dashboard

Dispositivos

Alertas

Reportes

Usuarios

Configuración

Documentación

Logs

Simulación

El item activo debe verse como en las capturas: fondo ligeramente más claro + barra cian a la izquierda.

Footer del sidebar:

Indicador “Sincronizado” con punto verde.

Botón “Cerrar sesión”.

Topbar:

Título de sección o breadcrumb.

A la derecha:

Chip con ubicación (ej: “Mina Norte”).

Icono de notificaciones con badge.

Avatar circular con iniciales.

Al hacer click en notificaciones, mostrar un panel como en la imagen 8:

Lista de notificaciones (pueden venir de backend si lo deseas o ser dummy por ahora).

Cada item: icono de severidad, texto y tiempo relativo.

Responsivo:

Desktop: sidebar siempre visible.

Tablet/Móvil: puedes permitir colapsar el sidebar (hamburger), pero mantén la lógica simple.

5. Tipos TypeScript (core/types)

Define al menos estos tipos (ajusta nombres de campos a las respuestas reales):

export interface ApiResponse<T> {
  success: boolean;
  message: string | null;
  data: T;
}

export interface User {
  id: number;
  name: string;
  lastName: string;
  email: string;
  role: 'ADMIN' | 'SUPERVISOR' | 'TECNICO' | 'OPERADOR';
  planId: number;
  status: 'ACTIVE' | 'DISABLED';
  lastLoginAt?: string;
}

export interface Plan {
  id: number;
  name: string;
  description: string;
}

export interface PlanFeature {
  id: number;
  featureKey: string;
  enabled: boolean;
}

export interface Device {
  id: number;
  deviceUid: string;
  name: string;
  status: 'ACTIVE' | 'WARNING' | 'CRITICAL' | 'OFFLINE';
  assignedUserId?: number | null;
  lastCommunicationAt?: string | null;
  lastBatteryLevel?: number | null;
  // opcional: últimos PM si backend los expone
}

export interface SensorConfig {
  id: number;
  sensorType: 'PM1' | 'PM25' | 'PM10';
  recommendedMax: number;
  criticalThreshold: number;
  unit: string;
}

export interface Reading {
  id: number;
  deviceId: number;
  recordedAt: string;
  pm1?: number | null;
  pm25?: number | null;
  pm10?: number | null;
  batteryLevel?: number | null;
  riskIndex: number;
  airQualityPercent: number;
}

export interface Alert {
  id: number;
  deviceId: number;
  readingId: number;
  severity: 'CRITICAL' | 'HIGH' | 'MEDIUM';
  status: 'PENDING' | 'IN_PROGRESS' | 'RESOLVED';
  message: string;
  createdAt: string;
  resolvedAt?: string | null;
}

export interface Incident {
  id: number;
  alertId: number;
  description: string;
  status: 'OPEN' | 'IN_PROGRESS' | 'CLOSED';
  createdById: number;
  createdAt: string;
  updatedAt?: string;
}

export interface UsageReport {
  id: number;
  generatedAt: string;
  totalUsers: number;
  totalDevices: number;
  totalReadings: number;
  totalAlerts: number;
  datosResumen: string; // o JSON.parse si lo modelas
}

export interface AccessLog {
  id: number;
  userId: number;
  action: string;
  ipAddress: string;
  createdAt: string;
}

export interface SystemLog {
  id: number;
  type: 'ERROR' | 'WARNING' | 'INFO';
  source: string;
  message: string;
  createdAt: string;
}

export interface ConfigParameter {
  id: number;
  key: string;
  value: string;
  createdAt: string;
  updatedAt: string;
}

export interface ConfigChangeLog {
  id: number;
  parameterKey: string;
  oldValue: string;
  newValue: string;
  changedById: number;
  changedAt: string;
}

export interface VersionHistory {
  id: number;
  versionNumber: string;
  description: string;
  releasedAt: string;
}

6. API client – core/api/*
6.1 httpClient.ts

Configura axios:

baseURL = "http://localhost:8080/api"

Interceptor de request:

Si hay token en localStorage (ej: airguardnet_token) o en contexto:

Añadir header Authorization: Bearer <token>.

Interceptor de response:

Si HTTP status es 401:

Limpiar sesión (borrar token en localStorage).

Redirigir a /login.

En otros errores:

Si la respuesta tiene formato ApiResponse, usar message como error.

Lanza errores claros para que la UI pueda mostrarlos.

6.2 authApi.ts

Funciones:

login(payload: { email: string; password: string }): Promise<ApiResponse<LoginData>>

POST /api/login.

register(...): opcional, usando POST /api/register.

6.3 Resto de APIs

Implementa wrappers tipados que DEVUELVAN data ya desempaquetado:

configApi.ts:

getConfigParameters() → GET /api/config-parameters.

updateConfigParameter(key, value) → PUT /api/config-parameters/{key}.

getSensorConfigs() → GET /api/sensor-configs.

updateSensorConfig(id, payload) → PUT /api/sensor-configs/{id}.

getConfigChangeLogs() → GET /api/config-change-logs.

reportApi.ts:

getUsageReports() → GET /api/usage-reports.

deviceApi.ts:

getDevices() → GET /api/devices.

getDeviceById(id) → GET /api/devices/{id}.

getDeviceReadings(id, params) → GET /api/devices/{id}/readings.

getDeviceAlerts(id, params) → GET /api/devices/{id}/alerts.

alertApi.ts:

getAlerts(params) → GET /api/alerts.

updateAlertStatus(id, status) → PATCH /api/alerts/{id}/status.

logsApi.ts (puedes crearlo):

getAccessLogs(params) → GET /api/access-logs.

getSystemLogs(params) → GET /api/system-logs.

versionApi.ts (si quieres separar):

getVersionHistory() → GET /api/version-history.

7. Auth – contexto, hook y rutas protegidas
7.1 AuthContext.tsx

Debe exponer:

user: User | null

token: string | null

loading: boolean

login(credentials): Promise<void>

logout(): void

Lógica de login:

Llamar a authApi.login.

Si success === false, lanzar error con message.

Si success === true:

Guardar token en estado y localStorage (airguardnet_token).

Guardar user (id, name, email, role, planId).

Redirigir a /dashboard.

Al inicializar la app:

Leer airguardnet_token de localStorage:

Si existe, restaurar token y user desde localStorage (y opcionalmente refrescar con /api/users/{id} si lo deseas).

7.2 useAuth.ts

Hook que devuelve el contexto:

const { user, token, login, logout, loading } = useAuth();

7.3 ProtectedRoute.tsx

Comportamiento:

Si token NO existe → redirigir a /login.

Si existe → renderizar children.

8. Routing & Layouts

En App.tsx / core/routing/routes.tsx configura:

Rutas públicas (bajo AuthLayout):

/login → LoginPage

/register (si se usa) → RegisterPage (puede mostrar sólo un mensaje “Solo administradores pueden registrar usuarios, TODO”)

/forgot-password → placeholder UI.

Rutas privadas (bajo MainLayout + ProtectedRoute):

/ y /dashboard → DashboardPage

/devices → DevicesListPage

/devices/:id → DeviceDetailPage

/alerts → AlertsCenterPage

/reports → ReportsPage

/users → UsersManagementPage

/config/system → SystemConfigPage

/config/sensors → SensorThresholdsPage

/config/logs → ConfigChangeLogPage

/docs → DocumentationPage

/docs/versions → VersionHistoryPage

/logs/access → AccessLogsPage

/logs/system → SystemLogsPage

/simulation → SimulationPage

El sidebar de MainLayout DEBE tener enlaces a todas estas rutas.

9. Módulos/pantallas – LÓGICA + DISEÑO como las imágenes
9.1 AUTH – LoginPage

UI:

Usa el layout de dos columnas descrito en 4.2 (copiando estilo de la imagen de login).

Campos: email, password.

Botón “Iniciar sesión”.

Lógica:

On submit → login(credentials).

Mostrar loading en el botón si loading es true.

Si error → mostrar mensaje del backend debajo del formulario (alerta MUI).

9.2 Dashboard – DashboardPage

Diseño inspirado en la primera captura:

Secciones:

Card superior “Estado actual”

Título: “Tu operación actual” o similar.

Texto con “Zona / Mina”.

Indicador “Conexión en tiempo real” con punto verde.

SummaryCards:

Consumir GET /api/usage-reports (último registro).

Mostrar:

Total de dispositivos.

Total de lecturas.

Alertas críticas abiertas.

Promedio de air_quality_percent (si datosResumen lo permite; o calcula con lecturas si tienes endpoint).

ChartsSection:

Gráfico de PM2.5 24h de un dispositivo principal usando GET /api/devices/{id}/readings.

Gráfico de distribución de alertas por severidad usando GET /api/alerts.

GlobalRiskIndex:

Calcula un índice global (0–100) a partir del riskIndex promedio de lecturas recientes.

Mostrar barra o donut, similar a las imágenes.

Todos los textos deben hablar de POLVO, no gases.

9.3 Devices – DevicesListPage y DeviceDetailPage

DevicesListPage:

Estilo inspirado en “Mi Dispositivo” pero para lista:

Grid o tabla de cards:

Nombre.

deviceUid.

Estado (chip).

Última comunicación (texto).

Nivel de batería (barra horizontal tipo 87% verde).

Datos desde GET /api/devices.

Filtro simple por status (chips: Todos, Activos, Warning, Críticos, Offline).

DeviceDetailPage:

Usa:

GET /api/devices/{id}

GET /api/devices/{id}/readings

GET /api/devices/{id}/alerts

Secciones:

Card principal dispositivo:

Nombre, UID, estado, usuario asignado, última comunicación.

Barra de batería como en la captura.

Gráfico de PM2.5 vs tiempo (últimas X horas/días).

Tabla de lecturas recientes.

Lista de alertas asociadas al dispositivo (cards pequeñas).

9.4 Alerts – AlertsCenterPage

Basado en la tercera imagen (“Historial de Alertas”):

KPIs arriba (cards horizontales):

Total Alertas.

Críticas.

Pendientes.

Resueltas.

Filtros con chips:

Todas

Críticas

Altas

Medias

Resueltas

Tabla/lista de alertas desde GET /api/alerts:

Nombre del dispositivo o UID.

Severidad (chip rojo/amarillo).

Estado (Pendiente, En Proceso, Resuelta).

Fecha/hora.

Mensaje (ej: “PM2.5 145 µg/m³ supera umbral 75 µg/m³”).

AlertDetailDrawer:

Al hacer click en una fila, abre un drawer lateral con detalles completos.

Permite cambiar status usando PATCH /api/alerts/{id}/status.

9.5 Reports – ReportsPage

Usa GET /api/usage-reports.

Secciones:

KpiCards:

totalUsers

totalDevices

totalReadings

totalAlerts

TimeSeriesCharts:

Gráfico de PM2.5 promedio por día (si datosResumen tiene esa info).

Si no, puedes producir un gráfico simple con datos transformados desde lecturas por dispositivo.

AlertDistributionCharts:

Barras/pie con cantidad de alertas por severidad y/o por día.

9.6 Users – UsersManagementPage

Endpoints:

GET /api/users

GET /api/plans

PATCH /api/users/{id}/status

(si existe POST /api/users, úsalo para crear; si no, deja TODO en el submit de alta).

UI:

Tabla con:

Nombre

Apellido

Email

Rol

Plan

Estado

Último login

Filtros por rol y por estado.

UserFormDialog:

Form para crear/editar usuario.

Si POST /api/users está disponible, úsalo; si no, deja un TODO claro pero la UI lista.

Al cambiar estado (toggle Active/Disabled) llama a PATCH /api/users/{id}/status.

9.7 Config – SystemConfigPage, SensorThresholdsPage, ConfigChangeLogPage

SystemConfigPage:

GET /api/config-parameters.

Tabla o formulario editable:

SYSTEM_NAME

DEFAULT_TIMEZONE

DEVICE_OFFLINE_MINUTES

Cualquier otro parámetro.

Guardar cada fila con PUT /api/config-parameters/{key}.

SensorThresholdsPage:

GET /api/sensor-configs.

Tabla:

sensorType (PM1, PM25, PM10).

recommendedMax.

criticalThreshold.

unit.

Editar umbrales en línea y guardar con PUT /api/sensor-configs/{id}.

Usa chips de color para indicar visualmente niveles (pero siempre polvo).

ConfigChangeLogPage:

GET /api/config-change-logs.

Tabla con:

parameterKey

oldValue

newValue

changedById mostrado como nombre de usuario si quieres, o id.

changedAt.

9.8 Docs – DocumentationPage, VersionHistoryPage

DocumentationPage:

Inspírate en la imagen del “Centro de Documentación”:

Panel izquierdo: categorías estáticas (Primeros pasos, Configuración de Dispositivos, Interpretación de datos de polvo, Gestión de alertas, etc.).

Panel derecho: mensaje de bienvenida, tarjetas de “Video Tutoriales” (contenido estático).

NO necesita backend para contenido, puedes hardcodear.

VersionHistoryPage:

GET /api/version-history.

Tabla con:

versionNumber

description

releasedAt

9.9 Logs – AccessLogsPage, SystemLogsPage

AccessLogsPage:

GET /api/access-logs.

Tabla:

Usuario (id o email).

Acción.

IP.

Fecha/hora.

SystemLogsPage:

GET /api/system-logs.

Tabla:

tipo (ERROR/WARNING/INFO) con chip de color.

source (user-service, device-service, gateway…).

message.

fecha/hora.

Filtros locales por tipo y fuente.

9.10 Simulation – SimulationPage

REGLA: simulación 100% frontend-only, sin endpoints nuevos.

SimulationControls:

Botones:

“Normal”

“Pico de polvo”

“Volver a normalidad”

Cambian parámetros de un generador de datos en memoria que crea valores de PM2.5.

SimulationLiveChart:

Gráfico en tiempo real con:

PM2.5

riskIndex (calculado en frontend usando la misma lógica de tramos que la especificación backend).

airQualityPercent = 100 - riskIndex.

No crear /api/simulation ni nada similar.

10. Manejo de errores y estados de carga

En TODAS las pantallas:

Mientras se carga data:

Mostrar skeletons / spinners MUI.

Si success === false o hay error de red:

Mostrar mensaje claro usando message (Snackbar o Alert).

Si el servidor devuelve 401:

Borrar token de localStorage.

Llamar logout().

Redirigir a /login.

11. REGLAS FINALES (no negociables)

NO tocar nada en backend/.

NO cambiar la estructura de carpetas ni nombres de archivos existentes en web/airguardnet-web.

Usar SIEMPRE el wrapper ApiResponse<T> con success, message, data.

NO inventar nuevos sensores ni magnitudes: sólo PM1, PM2.5, PM10, battery_level.

NO agregar módulos fuera de:

auth, dashboard, devices, alerts, reports, users, config, docs, logs, simulation.

(“Mis Tickets” y “Chat en Vivo” de las imágenes son sólo referencia visual, no módulos reales).

Usar exclusivamente los endpoints definidos en la especificación (lista del punto 2).
Si necesitas algo NO existente, deja // TODO bien documentado, pero no inventes rutas nuevas.

Mantener coherencia visual y de UX:

Tema oscuro.

Paleta y tipografía únicas.

Diseño de cards y chips alineado a las 10 imágenes.

El objetivo es que, al terminar:

npm install y npm run dev levanten la app sin errores.

Se pueda:

Iniciar sesión con usuarios reales.

Ver dashboard con datos reales donde haya endpoints.

Navegar por todos los módulos.

Leer y actualizar parámetros, thresholds y estados de alertas/usuarios.

Todo lo que depende de endpoints ya implementados funcione al 100%, como si la vida dependiera de ello.