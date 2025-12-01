package com.airguardnet.mobile.ui.realtimedemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val MAX_POINTS = 30

data class DemoReading(
    val timestamp: Long,
    val pm1: Float,
    val pm25: Float,
    val pm10: Float
)

data class RealtimeDemoState(
    val readings: List<DemoReading> = emptyList(),
    val current: DemoReading? = null,
    val isRunning: Boolean = true
)

@HiltViewModel
class RealtimeDemoViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(RealtimeDemoState())
    val state: StateFlow<RealtimeDemoState> = _state

    private var simulationJob: Job? = null

    init {
        startSimulation()
    }

    fun startSimulation() {
        if (simulationJob != null) return
        _state.update { it.copy(isRunning = true) }
        simulationJob = viewModelScope.launch {
            while (isActive) {
                val newReading = DemoReading(
                    timestamp = System.currentTimeMillis(),
                    pm1 = Random.nextFloat() * 80f,
                    pm25 = Random.nextFloat() * 150f,
                    pm10 = Random.nextFloat() * 200f
                )
                updateFromSource(newReading)
                delay(2500)
            }
        }
    }

    fun stopSimulation() {
        simulationJob?.cancel()
        simulationJob = null
        _state.update { it.copy(isRunning = false) }
    }

    fun toggleSimulation() {
        if (_state.value.isRunning) {
            stopSimulation()
        } else {
            startSimulation()
        }
    }

    fun clearHistory() {
        _state.update { it.copy(readings = emptyList(), current = null) }
    }

    fun updateFromSource(reading: DemoReading) {
        _state.update { current ->
            val newList = (current.readings + reading).takeLast(MAX_POINTS)
            current.copy(readings = newList, current = reading)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopSimulation()
    }
}
