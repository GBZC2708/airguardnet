# AirGuardNet Mobile

Aplicación Android para monitoreo de calidad de aire de la plataforma AirGuardNet. Está construida con Kotlin y Jetpack Compose siguiendo MVVM, con integración a backend vía Retrofit y caché local con Room.

## Arquitectura
- **Presentación:** Jetpack Compose + Navigation, ViewModels con `StateFlow`.
- **Inyección:** Hilt para dependencias en vistas, repositorios, workers y widgets.
- **Red:** Retrofit + Kotlinx Serialization para consumir el gateway `http://192.168.1.12:8080/api/`.
- **Persistencia:** Room para sesión, dispositivos, lecturas, alertas y hotspots.
- **Preferencias:** DataStore para flags locales (p.ej. notificaciones críticas).
- **Background:** WorkManager (`AlertPollingWorker`) para sondear alertas y disparar notificaciones.
- **Widgets:** Semáforo y Resumen leyendo últimos datos desde Room.
- **Mapas:** Google Maps + Location Services para centrar en la ubicación actual y mostrar hotspots locales.

## Configuración
1. Backend levantado en la laptop en `http://192.168.1.12:8080` (gateway expone `/api/**`).
2. Android necesita permisos de ubicación (`ACCESS_FINE_LOCATION`).
3. Configura tu clave de Google Maps en `AndroidManifest.xml` (`com.google.android.geo.API_KEY`).

## Ejecución
- Desde Android Studio: abrir el módulo `mobile/airguardnet-mobile` y ejecutar la app en dispositivo físico en la misma red WiFi.
- Por CLI: `./gradlew :airguardnet-mobile:app:assembleDebug` desde la raíz del monorepo.

## Funcionalidad principal
- **Login real** contra `/api/login`, sesión persistida con token.
- **Home** con semáforo de riesgo y última lectura.
- **Historial** de lecturas del dispositivo principal.
- **Alertas** sincronizadas desde backend, con notificaciones para severidad alta/crítica.
- **Mapa** con hotspots locales y centrado en la ubicación del usuario.
- **Widgets** de Semáforo y Resumen actualizados con datos locales.
- **Worker** que sondea alertas periódicamente respetando preferencias de notificación.
- **Perfil** mostrando datos de usuario, plan, dispositivo y cierre de sesión.
