package com.airguardnet.mobile.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.domain.model.Reading
import com.airguardnet.mobile.domain.usecase.ObserveReadingsUseCase
import com.airguardnet.mobile.domain.usecase.RefreshReadingsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class HistoryState(
    val readings: List<Reading> = emptyList(),
    val deviceId: Long? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val observeReadingsUseCase: ObserveReadingsUseCase,
    private val refreshReadingsUseCase: RefreshReadingsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state

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
            observeReadingsUseCase(deviceId).collect { list ->
                _state.value = _state.value.copy(readings = list)
            }
        }
    }

    fun refresh() {
        val deviceId = _state.value.deviceId ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            refreshReadingsUseCase(deviceId)
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}
