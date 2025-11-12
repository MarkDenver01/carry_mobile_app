package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.data.repository.AuthRepository
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _otpState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Loading())
    val otpState: StateFlow<NetworkResult<Unit>> = _otpState

    private val _verifyState = MutableStateFlow<NetworkResult<LoginResponse>>(NetworkResult.Loading())
    val verifyState: StateFlow<NetworkResult<LoginResponse>> = _verifyState

    fun sendOtp(mobileNumber: String) {
        viewModelScope.launch {
            _otpState.value = NetworkResult.Loading()
            _otpState.value = repository.sendOtp(mobileNumber)
        }
    }

    fun verifyOtp(mobileNumber: String, otp: String) {
        viewModelScope.launch {
            _verifyState.value = NetworkResult.Loading()
            _verifyState.value = repository.verifyOtp(mobileNumber, otp)
        }
    }
}
