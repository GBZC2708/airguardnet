package com.airguardnet.mobile.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.core.preferences.UserPreferencesManager
import com.airguardnet.mobile.data.mapper.toDomain
import com.airguardnet.mobile.data.repository.DeviceRepository
import com.airguardnet.mobile.core.utils.qualityPercent
import com.airguardnet.mobile.core.utils.resolveRiskBand
import com.airguardnet.mobile.domain.model.Reading
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveReadingsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
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

enum class HistoryFilter(val label: String) { TODAY("Hoy"), LAST_7_DAYS("Últimos 7 días"), ALL("Todo") }

data class ReadingUi(
    val deviceUid: String,
    val pm1: Double?,
    val pm25: Double?,
    val pm10: Double?,
    val batteryLevel: Double?,
    val timestampFormatted: String,
    val riskLabel: String,
    val statusColor: androidx.compose.ui.graphics.Color,
    val qualityPercent: Int
)

data class HistoryState(
    val readings: List<ReadingUi> = emptyList(),
    val deviceId: Long? = null,
    val selectedFilter: HistoryFilter = HistoryFilter.TODAY,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val observeReadingsUseCase: ObserveReadingsUseCase,
    private val refreshReadingsUseCase: RefreshReadingsUseCase,
    private val preferencesManager: UserPreferencesManager,
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state
    private var cachedReadings: List<Reading> = emptyList()
    private val formatter = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
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
                device?.let {
                    if (_state.value.deviceId != it.id) {
                        _state.update { current -> current.copy(deviceId = it.id) }
                        observe(it.id)
                    }
                } ?: _state.update { current -> current.copy(deviceId = null, readings = emptyList(), isEmpty = true) }
            }
        }
    }

    private fun observe(deviceId: Long) {
        viewModelScope.launch {
            observeReadingsUseCase(deviceId).collect { list ->
                cachedReadings = list
                applyFilter(_state.value.selectedFilter)
            }
        }
    }

    fun loadHistory(filter: HistoryFilter) {
        _state.update { it.copy(selectedFilter = filter) }
        applyFilter(filter)
        refresh()
    }

    private fun applyFilter(filter: HistoryFilter) {
        val now = System.currentTimeMillis()
        val filtered = cachedReadings.filter { reading ->
            when (filter) {
                HistoryFilter.TODAY -> isSameDay(reading.recordedAt, now)
                HistoryFilter.LAST_7_DAYS -> reading.recordedAt >= now - 7 * 24 * 60 * 60 * 1000L
                HistoryFilter.ALL -> true
            }
        }.map { it.toUi() }
        _state.update { it.copy(readings = filtered, isEmpty = filtered.isEmpty()) }
    }

    fun refresh() {
        viewModelScope.launch {
            val deviceId = _state.value.deviceId ?: run {
                val userId = currentUserId
                val assigned = userId?.let {
                    runCatching { deviceRepository.getAssignedDeviceForUser(it) }.getOrNull()
                }
                assigned?.id?.also { preferencesManager.setPrimaryDevice(it) }
                assigned?.toDomain()?.id
            } ?: return@launch

            _state.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { refreshReadingsUseCase(deviceId) }.onFailure { error ->
                _state.update { state ->
                    state.copy(
                        errorMessage = error.message ?: "No se pudo cargar el historial"
                    )
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun isSameDay(timestamp: Long, now: Long): Boolean {
        val dayMillis = 24 * 60 * 60 * 1000L
        return timestamp / dayMillis == now / dayMillis
    }

    private fun Reading.toUi(): ReadingUi {
        val band = resolveRiskBand(pm25)
        return ReadingUi(
            deviceUid = deviceId.toString(),
            pm1 = pm1,
            pm25 = pm25,
            pm10 = pm10,
            batteryLevel = batteryLevel,
            timestampFormatted = formatter.format(Date(recordedAt)),
            riskLabel = band.label,
            statusColor = band.color,
            qualityPercent = qualityPercent(pm25)
        )
    }
}
