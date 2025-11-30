package com.airguardnet.mobile.domain.usecase

import com.airguardnet.mobile.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}

class ObserveSessionUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.session
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
