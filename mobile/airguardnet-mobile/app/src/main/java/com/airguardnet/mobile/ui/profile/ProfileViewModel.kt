package com.airguardnet.mobile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.data.mapper.toDomain
import com.airguardnet.mobile.data.repository.DeviceRepository
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
import java.time.Instant
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
    val assignedDevice: com.airguardnet.mobile.data.remote.dto.DeviceDto? = null,
    val device: Device? = null,
    val planName: String = "",
    val criticalAlertsLast24h: Int = 0,
    val lastReadingSummary: String = "Sin lecturas",
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
    private val preferencesManager: UserPreferencesManager,
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state
    private val formatter = SimpleDateFormat("HH:mm dd/MM", Locale.getDefault())
    private var assignedLookupDone = false

    init {
        viewModelScope.launch {
            observeSessionUseCase()
                .combine(observeDevicesUseCase()) { session, devices ->
                    session to devices
                }.collect { (session, devices) ->
                    if (_state.value.session?.userId != session?.userId) {
                        assignedLookupDone = false
                    }
                    val assigned = resolveAssignedDevice(session, devices)
                    val device = assigned ?: devices.firstOrNull()
                    _state.update {
                        it.copy(
                            session = session,
                            device = device,
                            planName = session?.planId?.let { id -> "Plan #$id" } ?: ""
                        )
                    }
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

    private suspend fun resolveAssignedDevice(session: UserSession?, devices: List<Device>): Device? {
        val cached = session?.let { current -> devices.firstOrNull { it.assignedUserId == current.userId } }
        if (cached != null) {
            _state.update { it.copy(assignedDevice = it.assignedDevice ?: cached.toDtoFallback()) }
            preferencesManager.setPrimaryDevice(cached.id)
            return cached
        }
        val userId = session?.userId ?: return null
        if (!assignedLookupDone) {
            assignedLookupDone = true
            val assigned = deviceRepository.getAssignedDeviceForUser(userId)
            if (assigned != null) {
                preferencesManager.setPrimaryDevice(assigned.id)
                _state.update { it.copy(assignedDevice = assigned) }
                fetchLatestSummaries(assigned.id)
                return assigned.toDomain()
            }
        }
        return null
    }

    private fun subscribeDeviceData(deviceId: Long) {
        viewModelScope.launch {
            observeAlertsUseCase(deviceId).collect { alerts ->
                val last24h = alerts.count { alert ->
                    alert.severity.equals("CRITICAL", true) &&
                        (alert.createdAt ?: 0L) >= System.currentTimeMillis() - 24 * 60 * 60 * 1000L
                }
                _state.update { it.copy(criticalAlertsLast24h = last24h) }
            }
        }
        viewModelScope.launch {
            observeReadingsUseCase(deviceId).collect { readings ->
                val last = readings.firstOrNull()
                val summary = last?.pm25?.let { value ->
                    "PM2.5: $value µg/m³, ${formatter.format(Date(last.recordedAt))}"
                } ?: "Sin lecturas"
                _state.update { it.copy(lastReadingSummary = summary) }
            }
        }
    }

    private suspend fun fetchLatestSummaries(deviceId: Long) {
        val alertsResponse = runCatching { deviceRepository.getDeviceAlerts(deviceId) }.getOrNull()
        alertsResponse?.data?.let { alerts ->
            val cutoff = System.currentTimeMillis() - 24 * 60 * 60 * 1000L
            val last24h = alerts.count { dto ->
                dto.severity.equals("CRITICAL", true) && dto.createdAt.toEpochMillisOrNull()?.let { it >= cutoff } == true
            }
            _state.update { it.copy(criticalAlertsLast24h = last24h) }
        }
        val readingsResponse = runCatching { deviceRepository.getDeviceReadings(deviceId) }.getOrNull()
        val last = readingsResponse?.data?.firstOrNull()
        val summary = last?.pm25?.let { value ->
            "PM2.5: $value µg/m³, ${formatter.format(Date(last.recordedAt))}"
        } ?: "Sin lecturas"
        _state.update { it.copy(lastReadingSummary = summary) }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { refreshDevicesUseCase() }
            val deviceId = _state.value.assignedDevice?.id ?: _state.value.device?.id
            deviceId?.let { id ->
                runCatching { refreshAlertsUseCase(id) }
                runCatching { refreshReadingsUseCase(id) }
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

    private fun Device.toDtoFallback(): com.airguardnet.mobile.data.remote.dto.DeviceDto =
        com.airguardnet.mobile.data.remote.dto.DeviceDto(
            id = id,
            deviceUid = deviceUid,
            name = name,
            status = status,
            lastCommunicationAt = lastCommunicationAt.toIsoInstantOrNull(),
            lastBatteryLevel = lastBatteryLevel,
            assignedUserId = assignedUserId
        )

    private fun String?.toEpochMillisOrNull(): Long? {
        if (this.isNullOrBlank()) return null
        return runCatching { Instant.parse(this).toEpochMilli() }.getOrNull()
    }

    private fun Long?.toIsoInstantOrNull(): String? {
        if (this == null) return null
        return Instant.ofEpochMilli(this).toString()
    }
}
