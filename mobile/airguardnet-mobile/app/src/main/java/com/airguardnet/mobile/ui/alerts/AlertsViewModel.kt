package com.airguardnet.mobile.ui.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.domain.model.Alert
import com.airguardnet.mobile.domain.usecase.ObserveAlertsUseCase
import com.airguardnet.mobile.domain.usecase.RefreshAlertsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class AlertsState(
    val alerts: List<Alert> = emptyList(),
    val deviceId: Long? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val observeAlertsUseCase: ObserveAlertsUseCase,
    private val refreshAlertsUseCase: RefreshAlertsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AlertsState())
    val state: StateFlow<AlertsState> = _state

    init {
        viewModelScope.launch {
            observeSessionUseCase().filterNotNull().flatMapLatest { session ->
                observeDevicesUseCase().map { devices -> devices.firstOrNull { it.assignedUserId == session.userId } }
            }.collect { device ->
                if (device != null) {
                    _state.value = _state.value.copy(deviceId = device.id)
                    observe(device.id)
                }
            }
        }
    }

    private fun observe(deviceId: Long) {
        viewModelScope.launch {
            observeAlertsUseCase(deviceId).collect { alerts ->
                _state.value = _state.value.copy(alerts = alerts)
            }
        }
    }

    fun refresh() {
        val deviceId = _state.value.deviceId ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            refreshAlertsUseCase(deviceId)
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}
