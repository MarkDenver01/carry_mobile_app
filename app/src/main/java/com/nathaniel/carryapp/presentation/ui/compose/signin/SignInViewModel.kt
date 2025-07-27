package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.lifecycle.ViewModel
import com.nathaniel.carryapp.domain.request.SignInRequest
import com.nathaniel.carryapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    fun onSignIn(signInRequest: SignInRequest) {
        // TODO: validate and register
        _navigateTo.value = Routes.DASHBOARD
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }
}