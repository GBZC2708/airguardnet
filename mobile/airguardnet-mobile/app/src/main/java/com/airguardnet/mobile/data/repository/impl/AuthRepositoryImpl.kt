package com.airguardnet.mobile.data.repository.impl

import com.airguardnet.mobile.data.local.dao.UserSessionDao
import com.airguardnet.mobile.data.mapper.toDomain
import com.airguardnet.mobile.data.mapper.toEntity
import com.airguardnet.mobile.data.remote.AirGuardNetApiService
import com.airguardnet.mobile.data.remote.dto.LoginRequestDto
import com.airguardnet.mobile.domain.model.UserSession
import com.airguardnet.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class AuthRepositoryImpl(
    private val api: AirGuardNetApiService,
    private val userSessionDao: UserSessionDao
) : AuthRepository {
    override val session: Flow<UserSession?> = userSessionDao.observeSession().map { it?.toDomain() }

    override suspend fun login(email: String, password: String): Result<UserSession> = try {
        val response = api.login(LoginRequestDto(email, password))
        val data = response.data
        if (response.success && data != null) {
            val entity = data.toEntity()
            userSessionDao.clear()
            userSessionDao.insert(entity)
            Result.success(entity.toDomain())
        } else {
            Result.failure(IllegalStateException(response.message ?: "Credenciales inválidas"))
        }
    } catch (io: IOException) {
        Result.failure(IOException("No se pudo conectar al servidor", io))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout() {
        userSessionDao.clear()
    }
}
