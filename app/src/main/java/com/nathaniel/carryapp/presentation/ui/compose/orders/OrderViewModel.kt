package com.nathaniel.carryapp.presentation.ui.compose.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.data.repository.AuthRepository
import com.nathaniel.carryapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val login = authRepository.getCurrentSession()
            _isLoggedIn.value = login != null
        }
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }

    // ---------- BUTTON ACTIONS ----------

    fun onHomeClick() {
        if (_isLoggedIn.value == true) {
            _navigateTo.value = Routes.ORDERS
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onCategoriesClick() {
        if (_isLoggedIn.value == true) {
            //_navigateTo.value = Routes.CATEGORIES
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onReorderClick() {
        if (_isLoggedIn.value == true) {
            //_navigateTo.value = Routes.REORDER
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onAccountClick() {
        if (_isLoggedIn.value == true) {
           // _navigateTo.value = Routes.ACCOUNT
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onSearchClick() {
        if (_isLoggedIn.value == true) {
            // do nothing for now
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onViewMoreClick() {
        if (_isLoggedIn.value == true) {
            // do nothing for now
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }
}
