package com.airguardnet.mobile.data.remote.dto

data class AuthRequestDto(
    val email: String,
    val password: String
)

data class AuthResponseDto(
    val token: String,
    val userId: Long,
    val name: String,
    val email: String,
    val role: String,
    val planId: Long
) {
    data class UserDto(
        val id: Long,
        val name: String,
        val email: String,
        val role: String,
        val planId: Long
    )
}
