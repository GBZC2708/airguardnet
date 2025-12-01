# AirGuardNet Mobile

Aplicación Android para monitoreo de calidad de aire de la plataforma AirGuardNet. Se construye en Kotlin con Jetpack Compose siguiendo MVVM por capas y consume el backend existente sin modificaciones.

## Stack técnico
- **UI:** Jetpack Compose + Navigation.
- **Arquitectura:** MVVM con flujos de datos basados en `StateFlow` y `Flow`.
- **Inyección:** Hilt para vistas, repositorios, workers y widgets.
- **Red:** Retrofit + OkHttp + Kotlinx Serialization contra `http://192.168.1.12:8080/api/` (ver `core/network/ApiConstants.kt`).
- **Persistencia:** Room para sesión, dispositivos, lecturas, alertas y hotspots.
- **Preferencias:** DataStore para flags locales (p. ej. notificaciones críticas o dispositivo principal).
- **Background:** WorkManager (`AlertPollingWorker`) para sondear alertas y generar notificaciones locales.
- **Widgets:** Semáforo y Resumen que leen los últimos datos desde Room.
- **Mapas:** Google Maps + Location Services para centrar en la ubicación actual y mostrar hotspots reales.

## Requisitos previos
1. Backend levantado en `http://192.168.1.12:8080` (gateway expone `/api/**`).
2. Dispositivo físico o emulador en la misma red WiFi del backend.
3. Permisos de ubicación (`ACCESS_FINE_LOCATION`) concedidos para centrar el mapa.
4. Clave de Google Maps configurada en `AndroidManifest.xml` (`com.google.android.geo.API_KEY`).

> **Importante:** No cambiar `BASE_URL`; está fijada en `mobile/airguardnet-mobile/app/src/main/java/com/airguardnet/mobile/core/network/ApiConstants.kt`.

## Cómo correr la app
- **Android Studio:** Abrir el módulo `mobile/airguardnet-mobile` y ejecutar la app en un dispositivo/emulador conectado a la misma red.
- **CLI:** Desde la raíz del monorepo ejecutar `./gradlew :airguardnet-mobile:app:assembleDebug`.

## Flujo de sesión
- **Login real** contra `/api/login`, validando email y contraseña. Se persisten token, rol y datos básicos en Room/DataStore.
- Si la sesión es válida al abrir la app, se navega directo al Home; si el backend responde `401`, se limpian datos y se vuelve al login.
- **Cerrar sesión** desde Perfil limpia sesión, cancela workers y redirige al login.

## Pantallas principales
- **Home:** Semáforo personal con PM1/PM2.5/PM10 de la última lectura. Botón de actualizar que refresca lecturas; muestra mensaje claro si no hay dispositivo asignado. Preparado para mostrar batería cuando el backend la exponga.
- **Detalle de dispositivo:** Acceso desde "Ver detalle" en Home. Lista las últimas lecturas con gráfico simple y alertas recientes. Incluye modo demo para simular lecturas en memoria (marcado como DEMO y sin tocar Room/backend).
- **Historial:** Listado de lecturas del dispositivo principal con filtros rápidos (hoy, 24h, 7 días) y botón de actualizar que sincroniza con el backend. Mensaje amigable si no hay datos.
- **Alertas:** Lista real de alertas para el dispositivo principal según rol. Permite refrescar; muestra nivel, descripción, timestamp y PM detonantes. Mensaje claro cuando no hay alertas.
- **Mapa:** Solicita permisos y centra en la ubicación actual; fallback a coordenada por defecto si no hay permisos. Muestra hotspots reales con infocard al tocarlos.
- **Perfil:** Muestra nombre, email, rol, plan, dispositivo asignado, último acceso y estado de cuenta si lo expone el backend. Incluye switch de notificaciones críticas y botón de cerrar sesión.

## Selección de dispositivo principal
- Se usa `primaryDeviceId` en preferencias; si no existe, se toma el primer dispositivo asignado al usuario.
- Home, historial, alertas y widgets leen siempre este dispositivo. Si el usuario no tiene dispositivo asignado, se muestra mensaje y se deshabilitan acciones de refresco que dependan de él.

## Widgets
- **Semáforo:** Usa la última lectura del dispositivo principal para mostrar estado (Seguro/Precaución/Peligroso) y PM2.5 (o `--` si no hay datos).
- **Resumen:** Muestra nombre corto del usuario, PM2.5 y estado; indica si hay alertas críticas recientes.
- Se actualizan al entrar a la app, al refrescar lecturas y cuando el `AlertPollingWorker` detecta alertas críticas nuevas.

## AlertPollingWorker y notificaciones
- Ejecuta sondeos periódicos al backend para detectar alertas críticas nuevas comparadas con las persistidas.
- Respeta el switch de "notificaciones críticas" en preferencias: si está apagado, no muestra notificaciones del sistema.
- Al tocar una notificación se abre la app directamente en la pestaña de Alertas con la lista sincronizada.

## Notas adicionales
- No modificar el backend ni el frontend web desde este módulo; el cliente móvil solo consume los endpoints existentes.
- El código está listo para mapear campos adicionales del dispositivo (batería, estado online) cuando el backend los publique, usando propiedades nullables y mapeos de dominio/UI.
