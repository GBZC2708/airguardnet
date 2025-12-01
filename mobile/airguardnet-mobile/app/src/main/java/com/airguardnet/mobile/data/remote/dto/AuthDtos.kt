package com.airguardnet.mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponseDto(
    @SerialName("jwtToken") val jwtToken: String,
    val userId: Long,
    val name: String,
    val email: String,
    val role: String,
    val planId: Long
)
