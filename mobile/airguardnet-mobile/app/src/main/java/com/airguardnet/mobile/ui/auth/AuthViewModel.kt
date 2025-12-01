package com.airguardnet.mobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.core.network.InvalidCredentialsException
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.domain.usecase.LoginUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import android.util.Patterns
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false,
    val isInvalidCredentials: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val loggedInRole: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    observeSessionUseCase: ObserveSessionUseCase,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    init {
        viewModelScope.launch {
            observeSessionUseCase().collect { session ->
                _state.update { it.copy(isLoggedIn = session != null, loggedInRole = session?.role) }
            }
        }

        viewModelScope.launch {
            preferencesManager.preferences.collect { prefs ->
                _state.update { current ->
                    if (current.email.isBlank()) current.copy(email = prefs.lastEmail.orEmpty()) else current
                }
            }
        }
    }

    fun onEmailChanged(value: String) = _state.update {
        it.copy(email = value, emailError = null, error = null, isInvalidCredentials = false)
    }

    fun onPasswordChanged(value: String) = _state.update {
        it.copy(password = value, passwordError = null, error = null, isInvalidCredentials = false)
    }

    fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        _state.update { it.copy(emailError = null, passwordError = null) }
        val email = _state.value.email.trim()
        val pass = _state.value.password
        if (!validate(email, pass)) return
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    isNetworkError = false,
                    isInvalidCredentials = false
                )
            }
            val result = loginUseCase(email, pass)
            result.fold(
                onSuccess = {
                    preferencesManager.setLastEmail(email.trim())
                    _state.update { current ->
                        current.copy(
                            isLoading = false,
                            error = null,
                            isNetworkError = false,
                            isInvalidCredentials = false
                        )
                    }
                },
                onFailure = { error ->
                    val (message, isNetwork) = when (error) {
                        is IOException -> "No se pudo conectar al servidor. Revisa tu conexión." to true
                        is InvalidCredentialsException -> "Correo o contraseña incorrectos." to false
                        else -> "Ocurrió un error inesperado. Intenta nuevamente." to false
                    }
                    _state.update { current ->
                        current.copy(
                            isLoading = false,
                            error = message,
                            isNetworkError = isNetwork,
                            isInvalidCredentials = error is InvalidCredentialsException,
                            password = if (error is InvalidCredentialsException) "" else current.password,
                            passwordError = if (error is InvalidCredentialsException) "Correo o contraseña incorrectos." else current.passwordError
                        )
                    }
                }
            )
        }
    }

    private fun validate(email: String, password: String): Boolean {
        var isValid = true
        if (email.isBlank()) {
            _state.update { it.copy(emailError = "Ingresa tu correo") }
            isValid = false
        } else if (!isEmailValid(email)) {
            _state.update { it.copy(emailError = "Correo inválido") }
            isValid = false
        }

        if (password.isBlank()) {
            _state.update { it.copy(passwordError = "Ingresa tu contraseña") }
            isValid = false
        } else if (password.length < 6) {
            _state.update { it.copy(passwordError = "La contraseña debe tener al menos 6 caracteres") }
            isValid = false
        }
        return isValid
    }

    private fun isEmailValid(email: String): Boolean {
        val normalizedEmail = email.trim().lowercase(Locale.getDefault())
        return Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()
    }
}
