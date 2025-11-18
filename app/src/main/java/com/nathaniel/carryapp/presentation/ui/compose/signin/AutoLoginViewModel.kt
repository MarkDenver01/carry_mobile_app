package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AutoLoginEvent {
    object NavigateToSignIn : AutoLoginEvent()
    object NavigateToHome : AutoLoginEvent()
    data class ShowError(val message: String) : AutoLoginEvent()
}

data class AutoLoginState(
    val isChecking: Boolean = true,
    val isLoggedIn: Boolean = false
)

@HiltViewModel
class AutoLoginViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AutoLoginState())
    val uiState: StateFlow<AutoLoginState> = _uiState

    private val _eventFlow = MutableSharedFlow<AutoLoginEvent>()
    val eventFlow: SharedFlow<AutoLoginEvent> = _eventFlow

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {

            _uiState.update { it.copy(isChecking = true) }

            try {
                val login = repository.getCurrentSession()

                if (login == null) {
                    // No saved login → go to sign in
                    _uiState.update { it.copy(isChecking = false, isLoggedIn = false) }
                    _eventFlow.emit(AutoLoginEvent.NavigateToSignIn)
                    return@launch
                }

                // If login exists → navigate to home
                _uiState.update { it.copy(isChecking = false, isLoggedIn = true) }
                _eventFlow.emit(AutoLoginEvent.NavigateToHome)

            } catch (e: Exception) {
                _uiState.update { it.copy(isChecking = false) }
                _eventFlow.emit(AutoLoginEvent.ShowError("Auto-login error: ${e.message}"))
                _eventFlow.emit(AutoLoginEvent.NavigateToSignIn)
            }
        }
    }
}
