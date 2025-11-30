package com.airguardnet.mobile.domain.repository

import com.airguardnet.mobile.domain.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val session: Flow<UserSession?>
    suspend fun login(email: String, password: String): Result<UserSession>
    suspend fun logout()
}
