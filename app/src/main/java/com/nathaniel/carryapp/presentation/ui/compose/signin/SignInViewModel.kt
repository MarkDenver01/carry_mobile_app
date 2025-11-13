package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.data.repository.AuthRepository
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiEvent {
    data class NavigateToOtp(val mobile: String) : AuthUiEvent()

    data class NavigateToTerms(val mobile: String) : AuthUiEvent()
    object NavigateToHome : AuthUiEvent()
    data class ShowError(val message: String) : AuthUiEvent()
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val otpSent: Boolean = false,
    val verified: Boolean = false
)

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // -------------------------
    // UI STATE
    // -------------------------
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    // -------------------------
    // ONE-TIME UI EVENTS
    // -------------------------
    private val _eventFlow = MutableSharedFlow<AuthUiEvent>()
    val eventFlow: SharedFlow<AuthUiEvent> = _eventFlow


    // -------------------------
    // SEND OTP
    // -------------------------
    fun sendOtp(mobileNumber: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.sendOtp(mobileNumber)) {

                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, otpSent = true) }

                    // 🔥 one-time event (NO LEAKS)
                    _eventFlow.emit(AuthUiEvent.NavigateToOtp(mobileNumber))
                }

                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(AuthUiEvent.ShowError(result.message ?: "Failed to send OTP"))
                }

                else -> Unit
            }
        }
    }


    // -------------------------
    // VERIFY OTP
    // -------------------------
    fun verifyOtp(mobileNumber: String, otp: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.verifyOtp(mobileNumber, otp)) {

                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, verified = true) }
                    _eventFlow.emit(AuthUiEvent.NavigateToHome)
                }

                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(AuthUiEvent.ShowError(result.message ?: "Incorrect OTP"))
                }

                else -> Unit
            }
        }
    }
}
