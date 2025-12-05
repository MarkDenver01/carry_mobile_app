package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.AgreementMapper
import com.nathaniel.carryapp.domain.model.AgreementRequest
import com.nathaniel.carryapp.domain.model.LoginSessionMapper
import com.nathaniel.carryapp.domain.model.LoginSessionRequest
import com.nathaniel.carryapp.domain.usecase.CheckAgreementStatusUseCase
import com.nathaniel.carryapp.domain.usecase.CheckLoginSessionUseCase
import com.nathaniel.carryapp.domain.usecase.ClearAgreementStatusUseCase
import com.nathaniel.carryapp.domain.usecase.DeleteLoginSessionUseCase
import com.nathaniel.carryapp.domain.usecase.SaveAgreementUseCase
import com.nathaniel.carryapp.domain.usecase.SaveLoginSessionUseCase
import com.nathaniel.carryapp.domain.usecase.SaveMobileOrEmailUseCase
import com.nathaniel.carryapp.domain.usecase.VerifyOtpResult
import com.nathaniel.carryapp.domain.usecase.VerifyOtpUseCase
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class AuthUiEvent {
    data class NavigateToOtp(val mobile: String) : AuthUiEvent()
    data class NavigateToTerms(val mobileOrEmail: String) : AuthUiEvent()

    data class NavigateToDriver(val mobileOrEmail: String) : AuthUiEvent(

    )
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
    private val saveMobileOrEmailUseCase: SaveMobileOrEmailUseCase,
    private val saveAgreementUseCase: SaveAgreementUseCase,
    private val checkAgreementStatusUseCase: CheckAgreementStatusUseCase,
    private val saveLoginSessionUseCase: SaveLoginSessionUseCase,
    private val deleteLoginSessionUseCase: DeleteLoginSessionUseCase,
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<AuthUiEvent>()
    val eventFlow: SharedFlow<AuthUiEvent> = _eventFlow

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn = _isLoggedIn

    fun saveMobileOrEmail(mobileOrEmail: String) {
        saveMobileOrEmailUseCase.invoke(mobileOrEmail)
    }

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


    fun verifyOtp(mobileOrEmail: String, otp: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = verifyOtpUseCase(mobileOrEmail, otp)) {
                is VerifyOtpResult.DriverLogin -> {
                    val agreed = checkAgreementStatusUseCase(mobileOrEmail)
                    if (agreed == true) {
                        _eventFlow.emit(AuthUiEvent.NavigateToHome)
                    } else {
                        _eventFlow.emit(AuthUiEvent.NavigateToDriver(mobileOrEmail))
                    }
                }

                is VerifyOtpResult.CustomerLogin,
                is VerifyOtpResult.NewUser -> {
                    val agreed = checkAgreementStatusUseCase(mobileOrEmail)

                    _uiState.update { it.copy(isLoading = false, verified = true) }
                    if (agreed == true) {
                        _eventFlow.emit(AuthUiEvent.NavigateToHome)
                    } else {
                        _eventFlow.emit(AuthUiEvent.NavigateToTerms(mobileOrEmail))
                    }
                }

                is VerifyOtpResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(AuthUiEvent.ShowError(result.message))
                }
            }
        }
    }

    fun addAgreementStatus(email: String, agreementStatus: Boolean, navController: NavController) {
        viewModelScope.launch {
            val agreementRequest = AgreementRequest(email, agreementStatus)
            saveAgreementUseCase.invoke(
                AgreementMapper.toEntity(
                    agreementRequest
                )
            )

            navController.navigate(Routes.DELIVERY_AREA) {
                popUpTo(Routes.AGREEMENT_TERMS_PRIVACY) { inclusive = true }
            }
        }
    }

    fun saveLoginSession(email: String, session: Boolean) {
        viewModelScope.launch {
            val loginSessionRequest = LoginSessionRequest(email, session)
            saveLoginSessionUseCase.invoke(
                LoginSessionMapper.toEntity(
                    loginSessionRequest
                )
            )
            Timber.d("login session {$email, $session}")
        }
    }

    fun deleteLoginSession() {
        viewModelScope.launch { deleteLoginSessionUseCase.invoke() }
    }
}
