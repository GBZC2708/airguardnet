package com.airguardnet.mobile.ui.realtimelive

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val LIVE_HISTORY_LIMIT = 20

data class LiveReadingUi(
    val pm1: Double?,
    val pm25: Double?,
    val pm10: Double?,
    val batteryLevel: Double?,
    val recordedAtFormatted: String,
    val riskLabel: String,
    val statusColor: Color,
    val qualityPercent: Int
)

data class RealtimeLiveState(
    val device: Device? = null,
    val current: LiveReadingUi? = null,
    val history: List<LiveReadingUi> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isEmpty: Boolean = true
)

@HiltViewModel
class RealtimeLiveViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val observeReadingsUseCase: ObserveReadingsUseCase,
    private val refreshDevicesUseCase: RefreshDevicesUseCase,
    private val refreshReadingsUseCase: RefreshReadingsUseCase,
    preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(RealtimeLiveState())
    val state: StateFlow<RealtimeLiveState> = _state

    private var observeJob: Job? = null
    private var refreshJob: Job? = null
    private val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    init {
        viewModelScope.launch { refreshDevicesUseCase() }
        viewModelScope.launch {
            combine(
                observeSessionUseCase(),
                observeDevicesUseCase(),
                preferencesManager.preferences
            ) { session, devices, prefs ->
                val preferred = prefs.primaryDeviceId?.let { id -> devices.firstOrNull { it.id == id } }
                val owned = session?.let { user -> devices.firstOrNull { it.assignedUserId == user.userId } }
                preferred ?: owned ?: devices.firstOrNull()
            }.collect { device ->
                _state.update { it.copy(device = device, errorMessage = null) }
                device?.let { startLiveUpdates(it.id) }
            }
        }
    }

    private fun startLiveUpdates(deviceId: Long) {
        observeJob?.cancel()
        refreshJob?.cancel()

        observeJob = viewModelScope.launch {
            observeReadingsUseCase(deviceId).collect { readings ->
                val uiList = readings.sortedByDescending { it.recordedAt }.map { it.toUi() }
                _state.update { current ->
                    current.copy(
                        current = uiList.firstOrNull(),
                        history = uiList.take(LIVE_HISTORY_LIMIT),
                        isEmpty = uiList.isEmpty(),
                        isLoading = false
                    )
                }
            }
        }

        refreshJob = viewModelScope.launch {
            while (isActive) {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                runCatching { refreshReadingsUseCase(deviceId) }.onFailure {
                    _state.update { state ->
                        state.copy(errorMessage = "No se pudieron actualizar las lecturas en vivo")
                    }
                }
                _state.update { it.copy(isLoading = false) }
                delay(8000)
            }
        }
    }

    fun manualRefresh() {
        val deviceId = _state.value.device?.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { refreshReadingsUseCase(deviceId) }.onFailure {
                _state.update { state ->
                    state.copy(errorMessage = "No pudimos obtener lecturas; verifica tu conexión")
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun Reading.toUi(): LiveReadingUi {
        val band = resolveRiskBand(pm25)
        return LiveReadingUi(
            pm1 = pm1,
            pm25 = pm25,
            pm10 = pm10,
            batteryLevel = batteryLevel,
            recordedAtFormatted = formatter.format(Date(recordedAt)),
            riskLabel = band.label,
            statusColor = band.color,
            qualityPercent = qualityPercent(pm25)
        )
    }

    override fun onCleared() {
        super.onCleared()
        observeJob?.cancel()
        refreshJob?.cancel()
    }
}
