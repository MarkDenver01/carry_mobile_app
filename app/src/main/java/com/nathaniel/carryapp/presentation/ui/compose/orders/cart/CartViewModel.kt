package com.nathaniel.carryapp.presentation.ui.compose.orders.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.domain.model.CartDisplayItem
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.domain.request.CheckoutRequest
import com.nathaniel.carryapp.domain.response.OrderResponse
import com.nathaniel.carryapp.domain.usecase.AddToCartUseCase
import com.nathaniel.carryapp.domain.usecase.CheckoutUseCase
import com.nathaniel.carryapp.domain.usecase.ClearCartUseCase
import com.nathaniel.carryapp.domain.usecase.GetCartCountUseCase
import com.nathaniel.carryapp.domain.usecase.GetCartSummaryUseCase
import com.nathaniel.carryapp.domain.usecase.RemoveFromCartUseCase
import com.nathaniel.carryapp.presentation.ui.compose.orders.CartSummary
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val getCartCountUseCase: GetCartCountUseCase,
    private val getCartSummaryUseCase: GetCartSummaryUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartDisplayItem>>(emptyList())
    val cartItems: StateFlow<List<CartDisplayItem>> = _cartItems.asStateFlow()

    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _checkoutState = MutableStateFlow<NetworkResult<OrderResponse>?>(null)
    val checkoutState = _checkoutState

    init {
        // para may initial value agad pag pumasok sa app
        refreshCart()
    }

    /** Tawagin ito galing OrdersScreen pag meron ka nang product list */
    fun setProducts(list: List<Product>) {
        _products.value = list
        refreshCart()
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                addToCartUseCase(product.id)
                refreshCart()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun decrementProduct(product: Product) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // removeLatest sa DB (1 quantity)
                removeFromCartUseCase(product.id)
                refreshCart()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun refreshCart() {
        viewModelScope.launch {
            val summaries: List<CartSummary> = getCartSummaryUseCase()
            val productMap = _products.value.associateBy { it.id }

            val displayItems = summaries.mapNotNull { summary ->
                val product = productMap[summary.productId]
                product?.let {
                    CartDisplayItem(
                        productId = it.id,
                        name = it.name,
                        imageUrl = it.imageUrl,
                        weight = it.size,      // ginamit natin yung "size" as weight text
                        price = it.price,
                        qty = summary.qty,
                        subtotal = it.price * summary.qty
                    )
                }
            }

            _cartItems.value = displayItems
            _cartCount.value = getCartCountUseCase()
        }
    }

    fun addProductOriginalDomain(productId: Long) {
        viewModelScope.launch {
            addToCartUseCase(productId)
            refreshCart()
        }
    }

    fun removeProductOriginalDomain(productId: Long) {
        viewModelScope.launch {
            removeFromCartUseCase(productId)
            refreshCart()
        }
    }

    fun checkout(request: CheckoutRequest) {
        viewModelScope.launch {
            _checkoutState.value = NetworkResult.Loading()

            val result = checkoutUseCase(request)
            _checkoutState.value = result
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase.invoke()   // you must add this in usecase/repository
            refreshCart()
        }
    }
}