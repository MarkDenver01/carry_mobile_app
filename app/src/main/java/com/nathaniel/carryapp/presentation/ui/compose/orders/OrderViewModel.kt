package com.nathaniel.carryapp.presentation.ui.compose.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.domain.usecase.GetAllProductsUseCase
import com.nathaniel.carryapp.domain.usecase.ProductResult
import com.nathaniel.carryapp.domain.usecase.VerifyOtpUseCase
import com.nathaniel.carryapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val productsUseCase: GetAllProductsUseCase,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        checkLoginStatus()
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            when (val result = productsUseCase()) {
                is ProductResult.Success -> {
                    _products.value = result.products
                }
                is ProductResult.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val login = apiRepository.getCurrentSession()
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
//        if (_isLoggedIn.value == true) {
//            _navigateTo.value = Routes.CATEGORIES
//        } else {
//            _navigateTo.value = Routes.SIGN_IN
//        }
        _navigateTo.value = Routes.CATEGORIES
    }

    fun onReorderClick() {
        if (_isLoggedIn.value == true) {
            //_navigateTo.value = Routes.REORDER
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onAccountClick() {
//        if (_isLoggedIn.value == true) {
//
//        } else {
//            _navigateTo.value = Routes.SIGN_IN
//        }
        _navigateTo.value = Routes.ACCOUNT
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
