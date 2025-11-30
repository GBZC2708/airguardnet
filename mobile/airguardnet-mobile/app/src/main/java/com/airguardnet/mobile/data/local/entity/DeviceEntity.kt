package com.airguardnet.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey val id: Long,
    val deviceUid: String,
    val name: String,
    val status: String?,
    val lastCommunicationAt: Long?,
    val lastBatteryLevel: Double?,
    val assignedUserId: Long?
)
