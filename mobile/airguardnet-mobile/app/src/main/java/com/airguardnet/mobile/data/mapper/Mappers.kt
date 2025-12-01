package com.airguardnet.mobile.data.mapper

import com.airguardnet.mobile.data.local.entity.AlertEntity
import com.airguardnet.mobile.data.local.entity.DeviceEntity
import com.airguardnet.mobile.data.local.entity.HotspotEntity
import com.airguardnet.mobile.data.local.entity.ReadingEntity
import com.airguardnet.mobile.data.local.entity.UserSessionEntity
import com.airguardnet.mobile.data.remote.dto.AlertDto
import com.airguardnet.mobile.data.remote.dto.AuthResponseDto
import com.airguardnet.mobile.data.remote.dto.DeviceDto
import com.airguardnet.mobile.data.remote.dto.ReadingDto
import com.airguardnet.mobile.data.remote.dto.SensorConfigDto
import com.airguardnet.mobile.domain.model.Alert
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.model.Hotspot
import com.airguardnet.mobile.domain.model.Reading
import com.airguardnet.mobile.domain.model.SensorConfig
import com.airguardnet.mobile.domain.model.UserSession

fun AuthResponseDto.toEntity(): UserSessionEntity = UserSessionEntity(
    userId = userId ?: 0L,
    name = name.orEmpty(),
    email = email.orEmpty(),
    role = role.orEmpty(),
    planId = planId ?: 0L,
    jwtToken = token.orEmpty(),
    lastLoginAt = System.currentTimeMillis()
)

fun UserSessionEntity.toDomain() = UserSession(userId, name, email, role, planId, jwtToken, lastLoginAt)

fun DeviceDto.toEntity() = DeviceEntity(id, deviceUid, name, status, lastCommunicationAt, lastBatteryLevel, assignedUserId)
fun DeviceEntity.toDomain() = Device(id, deviceUid, name, status, lastCommunicationAt, lastBatteryLevel, assignedUserId)
fun DeviceDto.toDomain() = Device(id, deviceUid, name, status, lastCommunicationAt, lastBatteryLevel, assignedUserId)

fun ReadingDto.toEntity() = ReadingEntity(id, deviceId, recordedAt, pm1, pm25, pm10, batteryLevel, riskIndex, airQualityPercent)
fun ReadingEntity.toDomain() = Reading(id, deviceId, recordedAt, pm1, pm25, pm10, batteryLevel, riskIndex, airQualityPercent)

fun AlertDto.toEntity() = AlertEntity(id, deviceId, readingId, severity, status, message, createdAt, resolvedAt)
fun AlertEntity.toDomain() = Alert(id, deviceId, readingId, severity, status, message, createdAt, resolvedAt)

fun HotspotEntity.toDomain() = Hotspot(id, deviceId, recordedAt, pm25, severity, latitude, longitude)
fun Hotspot.toEntity() = HotspotEntity(id = if (id == 0L) 0 else id, deviceId = deviceId, recordedAt = recordedAt, pm25 = pm25, severity = severity, latitude = latitude, longitude = longitude)

fun SensorConfigDto.toDomain() = SensorConfig(id, name, pm25Warning, pm25Critical, pm10Warning, pm10Critical, riskIndexSafeMax, riskIndexWarningMax)
