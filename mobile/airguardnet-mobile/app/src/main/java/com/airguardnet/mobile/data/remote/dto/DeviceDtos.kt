package com.airguardnet.mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    @SerialName("id") val id: Long,
    @SerialName("deviceUid") val deviceUid: String,
    @SerialName("name") val name: String,
    @SerialName("status") val status: String? = null,
    @SerialName("lastCommunicationAt") val lastCommunicationAt: String? = null,
    @SerialName("lastBatteryLevel") val lastBatteryLevel: Double? = null,
    @SerialName("assignedUserId") val assignedUserId: Long? = null
)

@Serializable
data class ReadingDto(
    @SerialName("id") val id: Long,
    @SerialName("deviceId") val deviceId: Long,
    @SerialName("recordedAt") val recordedAt: String? = null,
    @SerialName("pm1") val pm1: Double? = null,
    @SerialName("pm25") val pm25: Double? = null,
    @SerialName("pm10") val pm10: Double? = null,
    @SerialName("batteryLevel") val batteryLevel: Double? = null,
    @SerialName("riskIndex") val riskIndex: Int,
    @SerialName("airQualityPercent") val airQualityPercent: Int
)

@Serializable
data class AlertDto(
    @SerialName("id") val id: Long,
    @SerialName("deviceId") val deviceId: Long,
    @SerialName("readingId") val readingId: Long,
    @SerialName("severity") val severity: String,
    @SerialName("status") val status: String,
    @SerialName("message") val message: String,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("resolvedAt") val resolvedAt: String? = null
)

@Serializable
data class ConfigParameterDto(
    @SerialName("id") val id: Long,
    @SerialName("key") val key: String,
    @SerialName("value") val value: String
)

@Serializable
data class SensorConfigDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("pm25Warning") val pm25Warning: Double? = null,
    @SerialName("pm25Critical") val pm25Critical: Double? = null,
    @SerialName("pm10Warning") val pm10Warning: Double? = null,
    @SerialName("pm10Critical") val pm10Critical: Double? = null,
    @SerialName("riskIndexSafeMax") val riskIndexSafeMax: Int? = null,
    @SerialName("riskIndexWarningMax") val riskIndexWarningMax: Int? = null
)
