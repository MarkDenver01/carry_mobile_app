package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.domain.request.CashInRequest
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.domain.response.WalletResponse
import com.nathaniel.carryapp.domain.usecase.CashInResult
import com.nathaniel.carryapp.domain.usecase.CashInUseCase
import com.nathaniel.carryapp.domain.usecase.GetCustomerDetailsUseCase
import com.nathaniel.carryapp.domain.usecase.GetWalletBalanceResult
import com.nathaniel.carryapp.domain.usecase.GetWalletBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val getCustomerDetailsUseCase: GetCustomerDetailsUseCase,
    private val cashInUseCase: CashInUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase
) : ViewModel() {

    private val _customerDetails = MutableStateFlow<CustomerDetailRequest?>(null)
    val customerDetails = _customerDetails

    private val _cashInUrl = MutableStateFlow<String?>(null)
    val cashInUrl = _cashInUrl

    private val _walletBalance = MutableStateFlow(0.0)
    val walletBalance = _walletBalance

    val isLoading = MutableStateFlow(false)

    init {
        loadSavedCustomerDetails()
    }

    private fun loadSavedCustomerDetails() {
        viewModelScope.launch {
            _customerDetails.value = getCustomerDetailsUseCase()
        }
    }

    fun onCashInClicked(amountStr: String) {
        val amount = amountStr.replace(",", "").toDoubleOrNull() ?: return
        val mobile = customerDetails.value?.mobileNumber ?: return
        val email = customerDetails.value?.email ?: "user@gmail.com"

        viewModelScope.launch {
            isLoading.value = true

            val req = CashInRequest(
                mobileNumber = mobile,
                amount = amount.toLong(),
                email = email
            )

            when (val result = cashInUseCase(req)) {
                is CashInResult.Success -> _cashInUrl.value = result.cashInResponse.invoiceUrl
                is CashInResult.Failed -> {
                    // TODO: Add toast
                }
            }
            isLoading.value = false
        }
    }

    fun clearCashInUrl() {
        _cashInUrl.value = null
    }

    fun refreshWallet() {
        val mobile = customerDetails.value?.mobileNumber ?: return

        Timber.tag("WALLET").i("Refreshing wallet for $mobile")

        viewModelScope.launch {
            isLoading.value = true
            when (val result = getWalletBalanceUseCase(mobile)) {

                is GetWalletBalanceResult.Success -> {
                    Timber.tag("WALLET").i("New balance: ${result.walletResponse.balance}")
                    _walletBalance.value = result.walletResponse.balance.toDouble()
                }

                is GetWalletBalanceResult.Failed -> {
                    Timber.tag("WALLET").e("Failed getting wallet")
                }
            }
            isLoading.value = false
        }
    }

}
