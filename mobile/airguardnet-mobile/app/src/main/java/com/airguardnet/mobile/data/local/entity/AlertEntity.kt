package com.airguardnet.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey val id: Long,
    val deviceId: Long,
    val readingId: Long,
    val severity: String,
    val status: String,
    val message: String,
    val createdAt: Long,
    val resolvedAt: Long?
)
