package com.airguardnet.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotspots")
data class HotspotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deviceId: Long,
    val recordedAt: Long,
    val pm25: Double,
    val severity: String,
    val latitude: Double,
    val longitude: Double
)
