package com.airguardnet.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_session")
data class UserSessionEntity(
    @PrimaryKey val userId: Long,
    val name: String,
    val email: String,
    val role: String,
    val planId: Long,
    val jwtToken: String,
    val lastLoginAt: Long
)
