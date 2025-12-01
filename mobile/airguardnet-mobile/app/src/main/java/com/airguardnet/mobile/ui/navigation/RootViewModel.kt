package com.airguardnet.mobile.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.domain.model.UserSession
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RootViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase
) : ViewModel() {
    val session: StateFlow<UserSession?> = observeSessionUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
