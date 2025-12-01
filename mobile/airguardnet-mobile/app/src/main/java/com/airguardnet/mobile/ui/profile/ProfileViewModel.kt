package com.airguardnet.mobile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.model.UserSession
import com.airguardnet.mobile.domain.usecase.LogoutUseCase
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileState(
    val session: UserSession? = null,
    val device: Device? = null,
    val criticalNotifications: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        viewModelScope.launch {
            observeSessionUseCase()
                .combine(observeDevicesUseCase()) { session, devices ->
                    val device = session?.let { current -> devices.firstOrNull { it.assignedUserId == current.userId } }
                        ?: devices.firstOrNull()
                    session to device
                }.collect { (session, device) ->
                    _state.update { it.copy(session = session, device = device) }
                }
        }
        viewModelScope.launch {
            preferencesManager.preferences.collect { prefs ->
                _state.update { it.copy(criticalNotifications = prefs.criticalNotificationsEnabled) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setCriticalNotifications(enabled) }
    }
}
