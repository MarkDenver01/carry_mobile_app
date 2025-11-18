package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.usecase.VerifyOtpResult
import com.nathaniel.carryapp.domain.usecase.VerifyOtpUseCase
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
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<AuthUiEvent>()
    val eventFlow: SharedFlow<AuthUiEvent> = _eventFlow


    fun sendOtp(mobileNumber: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.sendOtp(mobileNumber)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, otpSent = true) }
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


    fun verifyOtp(mobileNumber: String, otp: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = verifyOtpUseCase(mobileNumber, otp)) {

                is VerifyOtpResult.CustomerLogin -> {
                    _uiState.update { it.copy(isLoading = false, verified = true) }
                    _eventFlow.emit(AuthUiEvent.NavigateToTerms(mobileNumber))
                }

                is VerifyOtpResult.DriverLogin -> {
                    _uiState.update { it.copy(isLoading = false, verified = true) }
                    _eventFlow.emit(AuthUiEvent.NavigateToTerms(mobileNumber))
                }

                is VerifyOtpResult.NewUser -> {
                    _uiState.update { it.copy(isLoading = false, verified = true) }
                    _eventFlow.emit(AuthUiEvent.NavigateToTerms(mobileNumber))
                }

                is VerifyOtpResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(AuthUiEvent.ShowError(result.message))
                }
            }
        }
    }
}
