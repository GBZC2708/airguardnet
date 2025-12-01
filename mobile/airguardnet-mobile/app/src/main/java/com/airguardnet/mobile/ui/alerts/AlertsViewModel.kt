package com.airguardnet.mobile.ui.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.domain.model.Alert
import com.airguardnet.mobile.domain.usecase.ObserveAlertsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import com.airguardnet.mobile.domain.usecase.RefreshAlertsUseCase
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

enum class AlertFilter(val label: String) { ALL("Todas"), CRITICAL("Críticas"), OTHER("Otras") }

data class AlertUi(
    val id: String,
    val deviceUid: String?,
    val severity: String,
    val message: String,
    val timestampFormatted: String,
    val isCritical: Boolean
)

data class AlertsState(
    val alerts: List<AlertUi> = emptyList(),
    val deviceId: Long? = null,
    val selectedFilter: AlertFilter = AlertFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val observeAlertsUseCase: ObserveAlertsUseCase,
    private val refreshAlertsUseCase: RefreshAlertsUseCase,
    preferencesManager: UserPreferencesManager
) : ViewModel() {
    private val _state = MutableStateFlow(AlertsState())
    val state: StateFlow<AlertsState> = _state
    private var cachedAlerts: List<Alert> = emptyList()
    private val formatter = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())

    init {
        viewModelScope.launch {
            combine(
                observeSessionUseCase(),
                observeDevicesUseCase(),
                preferencesManager.preferences
            ) { _, devices, prefs ->
                prefs.primaryDeviceId?.let { id -> devices.firstOrNull { it.id == id } }
                    ?: devices.firstOrNull()
            }.collect { device ->
                device?.let {
                    _state.update { current -> current.copy(deviceId = it.id) }
                    observe(it.id)
                }
            }
        }
    }

    private fun observe(deviceId: Long) {
        viewModelScope.launch {
            observeAlertsUseCase(deviceId).collect { alerts ->
                cachedAlerts = alerts
                applyFilter(_state.value.selectedFilter)
            }
        }
    }

    fun loadAlerts(filter: AlertFilter) {
        _state.update { it.copy(selectedFilter = filter) }
        applyFilter(filter)
        refresh()
    }

    private fun applyFilter(filter: AlertFilter) {
        val filtered = when (filter) {
            AlertFilter.ALL -> cachedAlerts
            AlertFilter.CRITICAL -> cachedAlerts.filter { it.severity.equals("CRITICAL", true) }
            AlertFilter.OTHER -> cachedAlerts.filterNot { it.severity.equals("CRITICAL", true) }
        }.map { it.toUi() }
        _state.update { it.copy(alerts = filtered, isEmpty = filtered.isEmpty()) }
    }

    fun refresh() {
        val deviceId = _state.value.deviceId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { refreshAlertsUseCase(deviceId) }.onFailure { error ->
                _state.update { state -> state.copy(errorMessage = error.message ?: "No se pudieron cargar las alertas") }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun Alert.toUi(): AlertUi = AlertUi(
        id = id.toString(),
        deviceUid = deviceId.toString(),
        severity = severity,
        message = message,
        timestampFormatted = formatter.format(Date(createdAt)),
        isCritical = severity.equals("CRITICAL", true)
    )
}
