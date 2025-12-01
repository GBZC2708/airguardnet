package com.airguardnet.mobile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponseDto(
    val token: String? = null,
    val userId: Long? = null,
    val name: String? = null,
    val email: String? = null,
    val role: String? = null,
    val planId: Long? = null
)
