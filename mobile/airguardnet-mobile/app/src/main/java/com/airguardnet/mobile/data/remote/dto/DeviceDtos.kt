package com.airguardnet.mobile.data.remote.dto

data class DeviceDto(
    val id: Long,
    val deviceUid: String,
    val name: String,
    val status: String?,
    val lastCommunicationAt: Long?,
    val lastBatteryLevel: Double?,
    val assignedUserId: Long?
)

data class ReadingDto(
    val id: Long,
    val deviceId: Long,
    val recordedAt: Long,
    val pm1: Double?,
    val pm25: Double?,
    val pm10: Double?,
    val batteryLevel: Double?,
    val riskIndex: Int,
    val airQualityPercent: Int
)

data class AlertDto(
    val id: Long,
    val deviceId: Long,
    val readingId: Long,
    val severity: String,
    val status: String,
    val message: String,
    val createdAt: Long,
    val resolvedAt: Long?
)

data class ConfigParameterDto(
    val id: Long,
    val key: String,
    val value: String
)

data class SensorConfigDto(
    val id: Long,
    val name: String,
    val pm25Warning: Double?,
    val pm25Critical: Double?,
    val pm10Warning: Double?,
    val pm10Critical: Double?,
    val riskIndexSafeMax: Int?,
    val riskIndexWarningMax: Int?
)
