package com.airguardnet.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readings")
data class ReadingEntity(
    @PrimaryKey val id: Long,
    val deviceId: Long,
    val recordedAt: Long,
    val pm1: Double?,
    val pm25: Double?,
    val pm10: Double?,
    val batteryLevel: Double?,
    val riskIndex: Int,
    val airQualityPercent: Int
)
