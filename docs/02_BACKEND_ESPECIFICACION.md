# AirGuardNet – Especificación Backend

Este documento define con precisión:

- El modelo de datos a nivel de tablas y campos.
- La arquitectura por servicios lógicos (user-service, device-service, notification-service, api-gateway).
- Los casos de uso principales.
- Los endpoints REST esperados.
- Las reglas de seguridad y JWT.
- La lógica de negocio específica de polvo (PM1, PM2.5, PM10).

Es la guía directa para que una IA (Codex) implemente el backend sin salirse del alcance.

## 1. Stack backend y estructura de proyecto

Stack:

- Java 21
- Spring Boot 3.4.x
- Maven multi-módulo
- Arquitectura hexagonal
- PostgreSQL (una sola BD: airguardnet)
- Docker / Docker Compose (al menos para BD)

Estructura de módulos en `backend/airguardnet-backend`:

- common
- user-service
- device-service
- notification-service
- api-gateway

Patrón de paquetes por servicio:

- com.airguardnet.<service>.domain
  - model
  - repository
  - service
- com.airguardnet.<service>.application
  - usecase
- com.airguardnet.<service>.infrastructure
  - persistence
  - web
  - security (si aplica)
  - config

## 2. Modelo de datos detallado

Todos los servicios comparten una única base de datos PostgreSQL llamada airguardnet.

### 2.1 Tablas de usuarios y seguridad (user-service)

Tabla users:

- id (PK, bigserial)
- name (varchar)
- last_name (varchar)
- email (varchar, único)
- password_hash (varchar)
- role (enum o varchar: ADMIN, SUPERVISOR, TECNICO, OPERADOR)
- status (enum o varchar: ACTIVE, DISABLED)
- plan_id (FK → plans.id)
- last_login_at (timestamp)
- failed_login_count (int)
- locked_until (timestamp, nullable)
- created_at (timestamp)
- updated_at (timestamp)

Tabla plans:

- id (PK)
- name (varchar: Básico, Estándar, Avanzado)
- description (text)
- created_at (timestamp)
- updated_at (timestamp)

Tabla plan_features:

- id (PK)
- plan_id (FK → plans.id)
- feature_key (varchar, ejemplo: ADVANCED_REPORTS, MULTI_DEVICE_VIEW)
- enabled (boolean)

Tabla access_logs:

- id (PK)
- user_id (FK → users.id)
- action (varchar: LOGIN, LOGOUT, VIEW_DASHBOARD, etc.)
- ip_address (varchar)
- created_at (timestamp)

Tabla system_logs:

- id (PK)
- type (varchar: ERROR, WARNING, INFO)
- source (varchar: user-service, device-service, gateway)
- message (text)
- created_at (timestamp)

Tabla config_parameters:

- id (PK)
- key (varchar)
- value (text)
- created_at (timestamp)
- updated_at (timestamp)

Tabla config_change_logs:

- id (PK)
- parameter_key (varchar)
- old_value (text)
- new_value (text)
- changed_by_id (FK → users.id)
- changed_at (timestamp)

Tabla version_history:

- id (PK)
- version_number (varchar, ejemplo: v1.0.0)
- description (text)
- released_at (timestamp)

### 2.2 Tablas de dispositivos y sensores (device-service)

Tabla devices:

- id (PK)
- device_uid (varchar, único)
- name (varchar)
- status (varchar: ACTIVE, WARNING, CRITICAL, OFFLINE)
- assigned_user_id (FK → users.id, nullable)
- last_communication_at (timestamp)
- last_battery_level (double, 0–100)
- current_firmware_id (FK → firmware_versions.id, nullable)
- created_at (timestamp)
- updated_at (timestamp)

Tabla firmware_versions:

- id (PK)
- version_code (varchar, ejemplo: 1.0.3)
- description (text)
- recommended (boolean)
- created_at (timestamp)
- updated_at (timestamp)

Tabla sensor_configs:

- id (PK)
- sensor_type (varchar: PM1, PM25, PM10)
- recommended_max (double)
- critical_threshold (double)
- unit (varchar, esperado: µg/m³)
- created_by_id (FK → users.id)
- created_at (timestamp)
- updated_at (timestamp)

Tabla sensor_notes:

- id (PK)
- device_id (FK → devices.id)
- sensor_type (varchar: PM1, PM25, PM10)
- note_text (text)
- created_by_id (FK → users.id)
- created_at (timestamp)

Tabla readings:

- id (PK)
- device_id (FK → devices.id)
- recorded_at (timestamp, generado por el backend)
- pm1 (double, nullable)
- pm25 (double, nullable)
- pm10 (double, nullable)
- battery_level (double, nullable, 0–100)
- risk_index (int, 0–100)
- air_quality_percent (int, 0–100)

Tabla alerts:

- id (PK)
- device_id (FK → devices.id)
- reading_id (FK → readings.id)
- severity (varchar: CRITICAL, HIGH, MEDIUM)
- status (varchar: PENDING, IN_PROGRESS, RESOLVED)
- message (text)
- created_at (timestamp)
- resolved_at (timestamp, nullable)
- responsible_user_id (FK → users.id, nullable)

Tabla incidents:

- id (PK)
- alert_id (FK → alerts.id)
- description (text)
- status (varchar: OPEN, IN_PROGRESS, CLOSED)
- created_by_id (FK → users.id)
- created_at (timestamp)
- updated_at (timestamp)

Tabla usage_reports:

- id (PK)
- generated_at (timestamp)
- total_users (int)
- total_devices (int)
- total_readings (int)
- total_alerts (int)
- datos_resumen (JSON o text con JSON)

## 3. Casos de uso por servicio

### 3.1 user-service

Casos de uso principales:

- RegisterUserUseCase
- LoginUseCase
- LockUserAccountUseCase / UnlockUserAccountUseCase
- ListUsersUseCase
- UpdateUserUseCase
- ChangeUserStatusUseCase
- ListPlansUseCase
- ListPlanFeaturesUseCase
- ListAccessLogsUseCase
- ListSystemLogsUseCase
- UpdateConfigParameterUseCase (crea entrada en config_change_logs)
- ListConfigChangeLogsUseCase
- ListVersionHistoryUseCase

Reglas de negocio clave:

- Email único por usuario.
- Password:
  - Mínimo 8 caracteres.
  - Al menos una mayúscula, una minúscula y un dígito.
- Intentos fallidos:
  - Incrementar failed_login_count en login fallido.
  - Si se supera un umbral (por ejemplo 5), setear locked_until unos minutos en el futuro.
  - Este umbral y tiempo se pueden parametrizar via config_parameters.

### 3.2 device-service

Casos de uso principales:

- IngestReadingUseCase
- ListReadingsByDeviceAndRangeUseCase
- ListAlertsUseCase
- UpdateAlertStatusUseCase
- CreateIncidentUseCase
- UpdateIncidentUseCase
- ListDevicesUseCase
- CreateDeviceUseCase
- UpdateDeviceUseCase
- AssignDeviceToUserUseCase
- ListSensorConfigsUseCase
- UpdateSensorConfigUseCase
- GenerateUsageReportUseCase

Lógica detallada de IngestReadingUseCase:

1) Entrada:

```json
{
  "device_uid": "AG-ESP32-0001",
  "pm1": 12.5,
  "pm25": 48.2,
  "pm10": 85.0,
  "battery_level": 87.0,
  "timestamp": "2025-11-29T09:30:00Z"
}

```markdown
2) Validar:

- device_uid:
  - Buscar Device por `device_uid`.
  - Si no existe:
    - Rechazar la operación con error HTTP 400 o 404 (según se defina).
    - Registrar en `system_logs` un ERROR con:
      - type = ERROR
      - source = "device-service"
      - message = "Intento de lectura desde dispositivo desconocido: <device_uid>".

- Valores de PM:
  - Para cada `pm1`, `pm25`, `pm10`:
    - Si es `null`, se puede guardar como `null` (el sensor podría fallar temporalmente).
    - Si el valor < 0 → reemplazar por 0.
    - Si el valor > 2000 → reemplazar por 2000 y registrar WARNING en `system_logs` indicando que se recortó un valor extremo.

- battery_level:
  - Si viene informado:
    - Si < 0 → poner 0.
    - Si > 100 → poner 100.
  - Si no viene informado → dejar `null` en la lectura.

3) Calcular `risk_index`:

- PM base: usar siempre `pm25` como indicador principal.
- Obtener configuración de thresholds desde `sensor_configs` para `sensor_type = "PM25"`:
  - `recommended_max`
  - `critical_threshold`
- Si existe configuración:
  - Regla por tramos (ejemplo razonable):
    - Si `pm25 <= recommended_max`:
      - `risk_index` en el rango 0–25 (puede ser proporcional, por ejemplo mapeo lineal).
    - Si `recommended_max < pm25 < critical_threshold`:
      - `risk_index` en el rango 26–70 (mapeo lineal en ese tramo).
    - Si `pm25 >= critical_threshold`:
      - `risk_index` en el rango 71–100 (mapeo lineal en ese tramo).
- Si NO existe configuración para PM25:
  - Usar rangos fijos del dispositivo (fallback):
    - 0–35   → BAJO   → `risk_index` en 0–25.
    - 35–75  → MEDIO  → `risk_index` en 26–70.
    - 75–150 → ALTO   → `risk_index` en 71–90.
    - >150   → MUY ALTO → `risk_index` en 91–100.
  - Dejar un TODO indicando que se debe parametrizar vía `sensor_configs`.

4) Calcular `air_quality_percent`:

- Fórmula base:
  - `air_quality_percent = 100 - risk_index`
- Asegurar límites:
  - Si < 0 → poner 0.
  - Si > 100 → poner 100.

5) Guardar `reading`:

- Crear un registro en la tabla `readings` con:
  - `device_id` (FK al dispositivo encontrado).
  - `recorded_at` = timestamp actual del servidor (no el del payload).
  - `pm1`, `pm25`, `pm10` (ya normalizados).
  - `battery_level` (ya normalizado o `null`).
  - `risk_index`.
  - `air_quality_percent`.

6) Actualizar `device`:

- `last_communication_at` = timestamp actual del servidor.
- `last_battery_level` = `battery_level` de la lectura si no es `null`.
- `status`:
  - Derivado del `risk_index` de la nueva lectura:
    - Tramo seguro   → `ACTIVE`.
    - Tramo medio    → `WARNING`.
    - Tramo alto/crítico → `CRITICAL`.
- OFFLINE:
  - No se calcula aquí.
  - Un job (tarea programada) marcará `OFFLINE` si `now() - last_communication_at` supera un umbral definido en `config_parameters` (por ejemplo `DEVICE_OFFLINE_MINUTES`).  
  - Dejar un TODO indicando que esta lógica se implementa en un proceso separado.

7) Generar alerta (si aplica):

- Usar `pm25` y thresholds de `sensor_configs` (o los rangos de fallback).
- Regla:
  - Si `pm25 > critical_threshold`:
    - Crear `Alert` con:
      - severity = `CRITICAL`
      - status = `PENDING`
  - Si `pm25` entre `recommended_max` y `critical_threshold`:
    - Crear `Alert` con:
      - severity = `HIGH` (o `MEDIUM`, pero la regla debe quedar documentada).
  - Si `pm25 <= recommended_max`:
    - No crear alerta nueva.
- En todos los casos donde se crea alerta:
  - Vincular:
    - `device_id`
    - `reading_id`
  - `message`:
    - Texto tipo: `"PM2.5 en X µg/m³ supera umbral Y"`.

8) Respuesta HTTP esperada (a través de la capa web):

- Código: `201 Created`.
- Body (ejemplo):

```json
{
  "reading_id": 12345,
  "device_id": 10,
  "risk_index": 68,
  "air_quality_percent": 32,
  "device_status": "WARNING",
  "created_alert": {
    "id": 777,
    "severity": "HIGH",
    "status": "PENDING"
  }
}