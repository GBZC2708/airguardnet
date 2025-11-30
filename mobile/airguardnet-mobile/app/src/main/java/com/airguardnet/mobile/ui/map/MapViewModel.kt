package com.airguardnet.mobile.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.domain.model.Hotspot
import com.airguardnet.mobile.domain.usecase.ObserveHotspotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MapViewModel @Inject constructor(
    observeHotspotsUseCase: ObserveHotspotsUseCase
) : ViewModel() {
    val hotspots: StateFlow<List<Hotspot>> = observeHotspotsUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
