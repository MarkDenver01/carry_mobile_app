package com.nathaniel.carryapp.presentation.ui.compose.dashboard

import androidx.lifecycle.ViewModel
import com.nathaniel.carryapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    private val _userName = MutableStateFlow("User")
    val userName: StateFlow<String> = _userName

    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo

    private val _pushNotificationsEnabled = MutableStateFlow(true)
    val pushNotificationsEnabled: StateFlow<Boolean> = _pushNotificationsEnabled

    fun onDeliveryClick() {
        _navigateTo.value = Routes.DELIVERY
    }

    fun onPickupClick() {
        _navigateTo.value = Routes.PICKUP
    }

    fun onDisplayProfile() {
        _navigateTo.value = Routes.DISPLAY_PROFILE
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onSignOut() {
        // trigger navigation or other effects
    }

    fun onTogglePushNotifications(enabled: Boolean) {
        _pushNotificationsEnabled.value = enabled
        // persist state to preferences if needed
    }

    fun onChangePassword() {
        _navigateTo.value = Routes.CHANGE_PASSWORD
    }

    fun onMembership() {
        _navigateTo.value = Routes.MEMBERSHIP
    }

    fun onSelectPreferences() {
        // logic if needed
    }

    fun onVerifiedBadge() {
        _navigateTo.value = Routes.BADGE_DETAILS
    }

    fun onVoucher() {
        _navigateTo.value = Routes.VOUCHER
    }

}