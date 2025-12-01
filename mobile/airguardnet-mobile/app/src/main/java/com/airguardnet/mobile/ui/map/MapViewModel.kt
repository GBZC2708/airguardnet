package com.airguardnet.mobile.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.domain.model.Hotspot
import com.airguardnet.mobile.domain.usecase.ObserveHotspotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MapUiState(
    val hotspots: List<Hotspot> = emptyList(),
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
    val selectedHotspot: Hotspot? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class MapViewModel @Inject constructor(
    observeHotspotsUseCase: ObserveHotspotsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MapUiState())
    val state: StateFlow<MapUiState> = _state

    init {
        viewModelScope.launch {
            observeHotspotsUseCase()
                .catch { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "No se pudieron cargar los hotspots"
                        )
                    }
                }
                .collectLatest { list ->
                    _state.update { it.copy(hotspots = list, isLoading = false, errorMessage = null) }
                }
        }
    }

    fun setUserLocation(lat: Double, lng: Double) {
        _state.update { it.copy(userLatitude = lat, userLongitude = lng) }
    }

    fun selectHotspot(hotspot: Hotspot?) {
        _state.update { it.copy(selectedHotspot = hotspot) }
    }

    fun setError(message: String) {
        _state.update { it.copy(errorMessage = message, isLoading = false) }
    }
}
