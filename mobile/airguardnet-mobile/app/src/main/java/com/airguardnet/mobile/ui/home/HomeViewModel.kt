package com.airguardnet.mobile.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.data.mapper.toDomain
import com.airguardnet.mobile.data.repository.DeviceRepository
import com.airguardnet.mobile.core.utils.qualityPercent
import com.airguardnet.mobile.core.utils.resolveRiskBand
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.model.Reading
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveReadingsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import com.airguardnet.mobile.domain.usecase.RefreshDevicesUseCase
import com.airguardnet.mobile.domain.usecase.RefreshReadingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LatestReadingUi(
    val pm1: Double?,
    val pm25: Double?,
    val pm10: Double?,
    val batteryLevel: Double?,
    val timestampFormatted: String?,
    val riskLabel: String,
    val qualityPercent: Int,
    val statusColor: androidx.compose.ui.graphics.Color
)

data class HomeState(
    val device: Device? = null,
    val lastReading: LatestReadingUi? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val observeReadingsUseCase: ObserveReadingsUseCase,
    private val refreshDevicesUseCase: RefreshDevicesUseCase,
    private val refreshReadingsUseCase: RefreshReadingsUseCase,
    private val preferencesManager: UserPreferencesManager,
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state
    private var readingsJob: Job? = null
    private val dateFormatter = SimpleDateFormat("HH:mm / dd-MM-yyyy", Locale.getDefault())
    private var assignedLookupDone = false
    private var currentUserId: Long? = null

    init {
        viewModelScope.launch {
            combine(
                observeSessionUseCase(),
                observeDevicesUseCase(),
                preferencesManager.preferences
            ) { session, devices, prefs ->
                Triple(session, devices, prefs)
            }.collect { (session, devices, prefs) ->
                if (currentUserId != session?.userId) {
                    assignedLookupDone = false
                }
                currentUserId = session?.userId
                val remoteAssigned = if (!assignedLookupDone && session?.userId != null) {
                    assignedLookupDone = true
                    runCatching { deviceRepository.getAssignedDeviceForUser(session.userId) }.getOrNull()
                } else null
                remoteAssigned?.id?.let { preferencesManager.setPrimaryDevice(it) }
                val assigned = remoteAssigned?.toDomain()
                    ?: session?.let { user -> devices.firstOrNull { it.assignedUserId == user.userId } }
                val preferred = prefs.primaryDeviceId?.let { id -> devices.firstOrNull { it.id == id } }
                val device = preferred ?: assigned ?: devices.firstOrNull()
                _state.update { it.copy(device = device) }
                device?.let { listenReadings(it.id) }
            }
        }
        refresh()
    }

    private fun listenReadings(deviceId: Long) {
        readingsJob?.cancel()
        readingsJob = viewModelScope.launch {
            observeReadingsUseCase(deviceId).collect { readings ->
                val latest = readings.firstOrNull()
                _state.update {
                    it.copy(
                        lastReading = latest?.toUi(),
                        isEmpty = latest == null,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { refreshDevicesUseCase() }.onFailure {
                _state.update { state -> state.copy(errorMessage = "No se pudo cargar dispositivos") }
            }
            val deviceId = _state.value.device?.id ?: run {
                val userId = currentUserId
                val assigned = userId?.let { runCatching { deviceRepository.getAssignedDeviceForUser(it) }.getOrNull() }
                assigned?.id?.also { preferencesManager.setPrimaryDevice(it) }
                assigned?.toDomain()?.also { selected ->
                    _state.update { state -> state.copy(device = state.device ?: selected) }
                }?.id
            }
            deviceId?.let { id ->
                runCatching { refreshReadingsUseCase(id) }.onFailure { throwable ->
                    _state.update { state -> state.copy(errorMessage = throwable.message ?: "No se pudo cargar la última lectura") }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun Reading.toUi(): LatestReadingUi {
        val band = resolveRiskBand(pm25)
        return LatestReadingUi(
            pm1 = pm1,
            pm25 = pm25,
            pm10 = pm10,
            batteryLevel = batteryLevel,
            timestampFormatted = recordedAt.takeIf { it > 0 }?.let { dateFormatter.format(Date(it)) },
            riskLabel = band.label,
            qualityPercent = qualityPercent(pm25),
            statusColor = band.color
        )
    }
}
