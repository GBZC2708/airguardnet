package com.airguardnet.mobile.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.model.Reading
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveReadingsUseCase
import com.airguardnet.mobile.domain.usecase.RefreshDevicesUseCase
import com.airguardnet.mobile.domain.usecase.RefreshReadingsUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val device: Device? = null,
    val lastReading: Reading? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase,
    private val observeReadingsUseCase: ObserveReadingsUseCase,
    private val refreshDevicesUseCase: RefreshDevicesUseCase,
    private val refreshReadingsUseCase: RefreshReadingsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        val sessionFlow = observeSessionUseCase()
        viewModelScope.launch {
            sessionFlow.filterNotNull().flatMapLatest { session ->
                combine(
                    observeDevicesUseCase().map { list -> list.firstOrNull { it.assignedUserId == session.userId } },
                    observeDevicesUseCase()
                ) { assigned, _ -> assigned }
            }.collect { device ->
                _state.update { it.copy(device = device) }
                if (device != null) listenReadings(device.id)
            }
        }
        refresh()
    }

    private fun listenReadings(deviceId: Long) {
        viewModelScope.launch {
            observeReadingsUseCase(deviceId).collect { readings ->
                _state.update { it.copy(lastReading = readings.firstOrNull()) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            refreshDevicesUseCase()
            _state.value.device?.let { refreshReadingsUseCase(it.id) }
            _state.update { it.copy(isLoading = false) }
        }
    }
}
