package com.nathaniel.carryapp.presentation.ui.compose.voucher

import androidx.lifecycle.ViewModel
import com.nathaniel.carryapp.domain.model.Voucher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VoucherViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo

    fun onRedeem() {}

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun applyVoucher(voucher: Voucher) {
        // Add logic to apply the voucher
        // e.g., update points, show toast/snackbar, etc.
    }
}