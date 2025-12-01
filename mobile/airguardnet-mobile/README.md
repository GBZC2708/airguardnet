# AirGuardNet Mobile

Aplicación Android (Jetpack Compose + MVVM + Hilt + WorkManager + DataStore + Widgets) para monitoreo de calidad de aire. Esta guía permite a un profesor clonar, configurar y probar todas las funciones con backend real o en modo demo.

## Requerimientos
- **Android Studio recomendado:** Giraffe/Koala o superior.
- **SDK:** minSdk 26, targetSdk 34. Instalar desde el SDK Manager de Android Studio.
- **Dispositivo/emulador:** API 30+ (emulador Pixel 5/6 recomendado). En dispositivo físico usar la IP LAN del backend.
- **Clonar el repo:** `git clone https://.../airguardnet` y abrir `mobile/airguardnet-mobile`.

## Backend (resumen rápido)
- Base de datos `airguardnet` (PostgreSQL) con usuario `postgres` y contraseña `1234`.
- Microservicios levantados con gateway en `http://192.168.1.12:8080` (ajustar a tu LAN). La app ya usa `/api/**` vía gateway.
- No cambies `BASE_URL` (definido en `app/src/main/java/com/airguardnet/mobile/core/network/ApiConstants.kt`). Usa la IP LAN en el backend.

## Configuración de la app móvil
- Abrir el módulo `airguardnet-mobile` en Android Studio y sincronizar Gradle (no cambies versiones ni dependencias).
- Conceder permisos de ubicación y notificaciones en el dispositivo/emulador.
- Credenciales sugeridas para pruebas (ya existentes en backend):
  - admin@airguard.net / Admin123
  - supervisor@airguard.net / Supervisor123
  - tecnico@airguard.net / Tecnico123
  - operador@airguard.net / Operador123

## Flujo de pruebas por pantalla
- **Login:** ingresar credenciales. Verifica error con datos inválidos y éxito con las cuentas anteriores. Al iniciar sesión aparece notificación local (canal `session_channel`).
- **Home:** muestra semáforo personal con PM1/PM2.5/PM10 y batería de la última lectura. Botón “Actualizar” llama al backend. Si no hay dispositivo asignado, verás mensaje claro.
- **Historial:** lista de lecturas del dispositivo principal con filtros rápidos (hoy/24h/7d). Usa “Actualizar” para sincronizar.
- **Alertas:** filtra por severidad (Todas, Críticas, Otras). El filtro CRITICAL se abre automáticamente al tocar la notificación de alertas críticas.
- **Mapa:** acepta permisos; recentra en tu ubicación o usa fallback. Muestra hotspots reales.
- **Perfil:** muestra nombre, email, rol, plan, dispositivo asignado, última lectura y alertas 24h. Activa/desactiva “Notificaciones críticas” (afecta el worker de alertas). Desde aquí puedes cerrar sesión.

## Pantallas de tiempo real
- **Modo DEMO:** `Home -> Simulador en tiempo real (demo)` mantiene generación aleatoria en memoria, sin tocar backend.
- **Modo conectado:** `Home -> Tiempo real conectado (backend)`. Requiere backend activo y dispositivo con lecturas. Muestra PM1/PM2.5/PM10/batería en vivo, riesgo, calidad %, historial y estados (cargando/error/vacío). Botón “Actualizar ahora” fuerza un refresh; “Ir a modo DEMO” vuelve al simulador.

## Notificaciones y Worker
- **AlertPollingWorker:** corre cada ~15 min (WorkManager) y consulta alertas. Si hay sesión activa, notificaciones críticas habilitadas y nuevas alertas con severidad `CRITICAL`, envía una notificación agrupada (canal `critical_alerts_channel`). Al tocarla abre la pestaña Alertas con filtro CRITICAL.
- Para forzar prueba: genera alertas críticas en el backend para el dispositivo asignado, espera el ciclo del worker o ejecuta el worker manualmente desde Android Studio > Background Work.

## Widgets
- **Semáforo:** muestra PM2.5, estado de riesgo (color y etiqueta) y batería del dispositivo principal. Pulsa el widget para abrir Home.
- **Resumen:** muestra usuario actual, última PM2.5 y conteo de alertas críticas en 24h. Pulsa para abrir Alertas.
- Ambos widgets refrescan datos desde backend/Room cuando se añaden o al tocar el área.

## Notas y limitaciones
- El “tiempo real” es polling (no WebSocket). Intervalo ~8s en pantalla en vivo y 15 min en worker.
- Si el backend no está disponible, el modo demo sigue funcionando; las pantallas muestran estados vacíos/errores amigables.
- No cambies versiones de Kotlin/Compose/Gradle/Hilt/Retrofit ni `BASE_URL`.

## Comandos útiles
- Compilar en CLI: `./gradlew :app:assembleDebug`
- Limpiar datos de la app (si cambiaste de IP): desinstala la app o borra datos para resetear sesión y preferencias.
