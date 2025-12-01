package com.airguardnet.mobile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.model.UserSession
import com.airguardnet.mobile.domain.usecase.LogoutUseCase
import com.airguardnet.mobile.domain.usecase.ObserveAlertsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveReadingsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import com.airguardnet.mobile.domain.usecase.RefreshAlertsUseCase
import com.airguardnet.mobile.domain.usecase.RefreshDevicesUseCase
import com.airguardnet.mobile.domain.usecase.RefreshReadingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val session: UserSession? = null,
    val device: Device? = null,
    val planName: String = "",
    val criticalAlertsLast24h: Int = 0,
    val lastReadingSummary: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val criticalNotifications: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val observeAlertsUseCase: ObserveAlertsUseCase,
    private val observeReadingsUseCase: ObserveReadingsUseCase,
    private val refreshDevicesUseCase: RefreshDevicesUseCase,
    private val refreshAlertsUseCase: RefreshAlertsUseCase,
    private val refreshReadingsUseCase: RefreshReadingsUseCase,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state
    private val formatter = SimpleDateFormat("HH:mm dd/MM", Locale.getDefault())

    init {
        viewModelScope.launch {
            observeSessionUseCase()
                .combine(observeDevicesUseCase()) { session, devices ->
                    val device = session?.let { current -> devices.firstOrNull { it.assignedUserId == current.userId } }
                        ?: devices.firstOrNull()
                    session to device
                }.collect { (session, device) ->
                    _state.update { it.copy(session = session, device = device, planName = session?.planId?.let { id -> "Plan #$id" } ?: "") }
                    device?.let { subscribeDeviceData(it.id) }
                }
        }
        viewModelScope.launch {
            preferencesManager.preferences.collect { prefs ->
                _state.update { it.copy(criticalNotifications = prefs.criticalNotificationsEnabled) }
            }
        }
        refresh()
    }

    private fun subscribeDeviceData(deviceId: Long) {
        viewModelScope.launch {
            observeAlertsUseCase(deviceId).collect { alerts ->
                val last24h = alerts.count { it.severity.equals("CRITICAL", true) && it.createdAt >= System.currentTimeMillis() - 24 * 60 * 60 * 1000 }
                _state.update { it.copy(criticalAlertsLast24h = last24h) }
            }
        }
        viewModelScope.launch {
            observeReadingsUseCase(deviceId).collect { readings ->
                val last = readings.firstOrNull()
                val summary = last?.pm25?.let { value ->
                    "PM2.5: $value µg/m³, ${formatter.format(Date(last.recordedAt))}"
                }
                _state.update { it.copy(lastReadingSummary = summary) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { refreshDevicesUseCase() }
            _state.value.device?.let { device ->
                runCatching { refreshAlertsUseCase(device.id) }
                runCatching { refreshReadingsUseCase(device.id) }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setCriticalNotifications(enabled) }
    }
}
