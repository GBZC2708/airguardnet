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
    val isPasswordVisible: Boolean = false
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
                _state.update { it.copy(isLoggedIn = session != null) }
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
        it.copy(email = value, emailError = null)
    }

    fun onPasswordChanged(value: String) = _state.update {
        it.copy(password = value, passwordError = null)
    }

    fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
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
                    val message = when (error) {
                        is IOException -> "No se pudo conectar al servidor"
                        is InvalidCredentialsException -> error.message ?: "Credenciales incorrectas"
                        else -> error.message.takeUnless { it.isNullOrBlank() } ?: "Ocurrió un error inesperado"
                    }
                    _state.update { current ->
                        current.copy(
                            isLoading = false,
                            error = message,
                            isNetworkError = error is IOException,
                            isInvalidCredentials = error is InvalidCredentialsException
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
        }
        return isValid
    }

    private fun isEmailValid(email: String): Boolean {
        val normalizedEmail = email.trim().lowercase(Locale.getDefault())
        return Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()
    }
}
