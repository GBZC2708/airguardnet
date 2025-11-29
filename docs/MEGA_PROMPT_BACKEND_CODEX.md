# MEGA PROMPT BACKEND – AirGuardNet (para Codex)

Eres una IA de desarrollo que va a trabajar **solo en el backend** de AirGuardNet.

Este archivo es tu contrato.  
Si algo no está aquí o en `01_DOCUMENTO_MAESTRO_RESUMEN.md` y `02_BACKEND_ESPECIFICACION.md`, NO LO INVENTES.

El repo ya existe en GitHub y la estructura está creada. Tu trabajo es **completar el backend** respetando:

- El alcance funcional AirGuardNet (polvo con PMS5003).
- El modelo de datos definido.
- La arquitectura hexagonal por servicio.
- El uso de JWT.
- Una sola base de datos PostgreSQL (`airguardnet`).
- La estructura Maven multi-módulo existente.

No debes tocar el frontend (`web/airguardnet-web`) en esta fase.

---

## 1. Alcance fijo del proyecto (recordatorio)

Proyecto: **AirGuardNet**

Enfoque:

- Monitoreo de polvo en minería usando SOLO **PMS5003**.
- Magnitudes: **PM1, PM2.5, PM10** y `battery_level`.
- NO hay gases, NO hay temperatura, NO hay otros sensores.

Dispositivo:

- ESP32 + PMS5003 + LED + buzzer + motor vibrador + batería LiPo.
- Cada cierto tiempo envía al backend vía HTTP:
  - `device_uid`
  - `pm1`, `pm25`, `pm10`
  - `battery_level` (0–100)
  - `timestamp` (opcional)

Backend:

- Spring Boot 3.4.x
- Java 21
- Arquitectura hexagonal
- PostgreSQL (DB `airguardnet`)
- JWT
- Docker (al menos para la BD)
- Servicios lógicos:
  - `user-service`
  - `device-service`
  - `notification-service` (esqueleto)
  - `api-gateway`

TODO lo funcional gira alrededor de polvo respirable.  
NO agregues sensores ni módulos nuevos.

---

## 2. Estructura REAL del backend (ya creada)

Debes trabajar sobre esta estructura existente, **sin renombrar módulos ni romper el multi-módulo**.

```text
backend/
  airguardnet-backend/
    pom.xml                 (POM padre, packaging pom)
    common/
    user-service/
    device-service/
    notification-service/
    api-gateway/
