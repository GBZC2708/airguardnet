package com.airguardnet.mobile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airguardnet.mobile.domain.model.Device
import com.airguardnet.mobile.domain.model.UserSession
import com.airguardnet.mobile.domain.usecase.LogoutUseCase
import com.airguardnet.mobile.domain.usecase.ObserveDevicesUseCase
import com.airguardnet.mobile.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileState(
    val session: UserSession? = null,
    val device: Device? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    observeSessionUseCase: ObserveSessionUseCase,
    observeDevicesUseCase: ObserveDevicesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        viewModelScope.launch {
            observeSessionUseCase().collect { session ->
                _state.update { it.copy(session = session) }
                session?.let { current ->
                    observeDevicesUseCase().map { list -> list.firstOrNull { it.assignedUserId == current.userId } }
                        .collect { device -> _state.update { it.copy(device = device) } }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }
}
