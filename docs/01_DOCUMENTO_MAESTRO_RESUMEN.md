# AirGuardNet – Documento Maestro (Resumen Ejecutivo)

Este documento resume el contexto funcional y técnico del proyecto AirGuardNet.  
Es la referencia fija para:

- Chat 1: backend + web
- Chat 2: app móvil
- Chat 3: pruebas

No se permite cambiar el alcance:  
AirGuardNet se centra exclusivamente en polvo respirable usando el sensor PMS5003 (PM1, PM2.5, PM10).

## 1. Visión global del proyecto

Nombre: AirGuardNet

Objetivo general:  
Construir un sistema completo de monitoreo de polvo para minería que:

- Usa un dispositivo portátil con ESP32 + sensor PMS5003 para medir:
  - PM1.0
  - PM2.5
  - PM10
- Alerta al trabajador mediante:
  - LED de alta visibilidad
  - Buzzer (sonoro)
  - Motor vibrador (alerta táctil)
- Envía lecturas periódicas al servidor vía WiFi.
- Permite monitoreo centralizado desde:
  - Web app para administrador/supervisor/técnico.
  - App móvil para el trabajador.
- Analiza tendencias, genera alertas por niveles de polvo y produce reportes.

Tecnologías objetivo:

- Backend: Spring Boot + JWT + arquitectura hexagonal + PostgreSQL + Docker
- Web: Vite + React + TypeScript + MUI
- App móvil: Android (MVVM, APIs, Room, Firebase) [se aborda en otro documento/chat]
- Pruebas: HU completas, pruebas unitarias, integración, sistema, Docker Compose

## 2. Dispositivo portátil (firmware y hardware lógico)

Hardware lógico:

- Microcontrolador: ESP32 con WiFi 2.4 GHz.
- Sensor de polvo: PMS5003 (vía UART).
  - Mide: PM1.0, PM2.5, PM10 (µg/m³).
- Sistema de alertas locales:
  - LED frontal de alta visibilidad.
  - Buzzer mediante transistor Q1.
  - Motor vibrador mediante transistor Q2 y diodo flyback.
- Alimentación:
  - Batería LiPo 3.7V.
  - Módulo TP4056 (carga y protección).
  - MT3608 (step-up a 5V).
  - Monitor de batería modelado como porcentaje 0–100 % a nivel de firmware/backend.

Ciclo básico del firmware:

- Cada N segundos:
  - Leer PM1, PM2.5, PM10 desde PMS5003.
  - Validar trama y checksum.
  - Calcular índice de riesgo local basado en PM2.5:
    - 0–35 µg/m³ → riesgo BAJO
    - 35–75 µg/m³ → MEDIO
    - 75–150 µg/m³ → ALTO
    - >150 µg/m³ → MUY ALTO
  - Accionar LED, buzzer y vibrador según nivel de riesgo.
  - Enviar lectura al backend vía HTTP:
    - device_uid
    - pm1, pm25, pm10
    - battery_level
    - timestamp opcional

Recepción de comandos desde backend (modelo lógico):

- Comandos posibles:
  - TEST_LED
  - TEST_BUZZER
  - TEST_VIBRATION
  - SILENCE (silenciar buzzer temporalmente)
  - RESET_ALERTS
- Modo tester interno para diagnóstico (secuencia de prueba LED/buzzer/vibrador/PMS).

## 3. Arquitectura general del sistema

Componentes:

- Backend AirGuardNet (Spring Boot + JWT + Hexagonal + PostgreSQL + Docker).
- Web app AirGuardNet (dashboard administrativo).
- App móvil Android (trabajador, lecturas personales y alertas).
- ESP32 + PMS5003 (dispositivo físico).
- Infraestructura de pruebas (JUnit, Postman, Selenium, Docker Compose).

Microservicios lógicos en el backend (en un solo repo Maven multi-módulo):

- user-service
  - Usuarios, autenticación, roles, planes, logs de acceso, configuración global.
- device-service
  - Dispositivos, lecturas, alertas, incidencias, configuración de umbrales, notas técnicas, firmware.
- notification-service
  - Notificaciones internas y push hacia la app móvil (via FCM más adelante).
- api-gateway
  - Entrada única /api/**, validación de JWT y enrutamiento a servicios.

Base de datos:

- Una única base PostgreSQL.
- Nombre: airguardnet.

## 4. Modelo de datos de alto nivel

Tablas clave:

- Usuarios y seguridad:
  - users, plans, plan_features, access_logs, system_logs
- Dispositivos y sensores:
  - devices, firmware_versions, sensor_configs, sensor_notes, readings
- Alertas, incidencias y reportes:
  - alerts, incidents, config_parameters, config_change_logs, version_history, usage_reports

Este modelo se detalla al nivel de columnas/campos en el archivo 02_BACKEND_ESPECIFICACION.md.

## 5. Lógica de polvo y alertas (resumen)

- Las lecturas tratadas son exclusivamente PM1, PM2.5 y PM10 desde PMS5003.
- El indicador principal para riesgo es PM2.5.
- Se calcula:
  - risk_index (0–100) basado en PM2.5 y thresholds configurables.
  - air_quality_percent (0–100) como inverso del riesgo (100 = excelente).
- Se generan alertas según comparaciones PM2.5 vs recommended_max y critical_threshold.
- El estado del dispositivo (ACTIVE, WARNING, CRITICAL, OFFLINE) se basa en:
  - risk_index.
  - tiempo desde la última comunicación (OFFLINE se calcula por job según parámetro global).

## 6. Web app (vista general)

- Tema oscuro con:
  - Fondo azul muy oscuro.
  - Cards ligeramente más claras.
  - Acentos cian/neón.
  - Estados en verde, amarillo, rojo.
- Pantallas principales:
  - Login / Registro / Recuperar contraseña.
  - Dashboard de monitoreo.
  - Mis dispositivos.
  - Centro de alertas.
  - Análisis y reportes.
  - Gestión de usuarios.
  - Configuración (parámetros y thresholds).
  - Documentación e historial de versiones.
  - Soporte / logs.
  - Simulación (lecturas simuladas de polvo).

El detalle de pantallas, endpoints y flujos se define en 03_FRONTEND_ESPECIFICACION.md.

## 7. Reglas de alcance

- No se agregan sensores nuevos.
- No se agregan magnitudes de medición distintas a:
  - PM1
  - PM2.5
  - PM10
  - battery_level
- No se agregan módulos extra fuera de lo descrito.
- Cualquier ambigüedad se resuelve pidiendo aclaración o dejando TODO con comentario controlado.
