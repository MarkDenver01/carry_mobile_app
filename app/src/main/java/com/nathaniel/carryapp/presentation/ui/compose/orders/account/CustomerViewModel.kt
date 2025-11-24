package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.domain.usecase.GetCustomerDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val getCustomerDetailsUseCase: GetCustomerDetailsUseCase,
) : ViewModel() {
    private val _customerDetails = MutableStateFlow<CustomerDetailRequest?>(null)
    val customerDetails: StateFlow<CustomerDetailRequest?> = _customerDetails

    init {
        loadSavedCustomerDetails()
    }

    private fun loadSavedCustomerDetails() {
        viewModelScope.launch {
            val saved = getCustomerDetailsUseCase()
            _customerDetails.value = saved
        }
    }
}