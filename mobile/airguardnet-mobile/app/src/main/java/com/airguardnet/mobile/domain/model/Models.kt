package com.airguardnet.mobile.domain.model

data class UserSession(
    val userId: Long,
    val name: String,
    val email: String,
    val role: String,
    val planId: Long,
    val token: String,
    val lastLoginAt: Long
)

data class Device(
    val id: Long,
    val deviceUid: String,
    val name: String,
    val status: String?,
    val lastCommunicationAt: Long?,
    val lastBatteryLevel: Double?,
    val assignedUserId: Long?
)

data class Reading(
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

data class Alert(
    val id: Long,
    val deviceId: Long,
    val readingId: Long,
    val severity: String,
    val status: String,
    val message: String,
    val createdAt: Long?,
    val resolvedAt: Long?
)

data class Hotspot(
    val id: Long,
    val deviceId: Long,
    val recordedAt: Long,
    val pm25: Double,
    val severity: String,
    val latitude: Double,
    val longitude: Double
)

data class SensorConfig(
    val id: Long,
    val name: String,
    val pm25Warning: Double?,
    val pm25Critical: Double?,
    val pm10Warning: Double?,
    val pm10Critical: Double?,
    val riskIndexSafeMax: Int?,
    val riskIndexWarningMax: Int?
)
