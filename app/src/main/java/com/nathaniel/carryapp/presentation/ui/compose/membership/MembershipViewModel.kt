package com.nathaniel.carryapp.presentation.ui.compose.membership

import androidx.lifecycle.ViewModel
import com.nathaniel.carryapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MembershipViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onAvailableNow() {
        _navigateTo.value = Routes.SUBSCRIPTION
    }

    fun onGcashClick() {

    }

    fun onMayaClick() {

    }
}