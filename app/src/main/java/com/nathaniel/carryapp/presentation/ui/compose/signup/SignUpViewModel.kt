package com.nathaniel.carryapp.presentation.ui.compose.signup

import androidx.lifecycle.ViewModel
import com.nathaniel.carryapp.domain.request.SignUpRequest
import com.nathaniel.carryapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    fun onSignUp(signUpRequest: SignUpRequest) {
        // TODO: validate and register
        _navigateTo.value = Routes.DASHBOARD
    }

    fun onContinueAsGuest() {
        _navigateTo.value = Routes.DASHBOARD
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onAuthGoogleSignUp() {
        // TODO: validate and register
        _navigateTo.value = Routes.DASHBOARD
    }

    fun onAuthFacebookSignUp() {
        // TODO: validate and register
        _navigateTo.value = Routes.DASHBOARD
    }
}