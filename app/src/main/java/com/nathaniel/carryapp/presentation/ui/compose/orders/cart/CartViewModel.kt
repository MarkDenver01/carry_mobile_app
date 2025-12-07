package com.nathaniel.carryapp.presentation.ui.compose.orders.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.data.local.room.entity.ReorderEntity
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
import com.nathaniel.carryapp.domain.usecase.SaveReorderHistoryUseCase
import com.nathaniel.carryapp.presentation.ui.compose.orders.CartSummary
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val getCartCountUseCase: GetCartCountUseCase,
    private val getCartSummaryUseCase: GetCartSummaryUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val saveReorderHistoryUseCase: SaveReorderHistoryUseCase,
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartDisplayItem>>(emptyList())
    val cartItems: StateFlow<List<CartDisplayItem>> = _cartItems.asStateFlow()

    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _checkoutState = MutableSharedFlow<NetworkResult<OrderResponse>>(replay = 0)
    val checkoutState = _checkoutState.asSharedFlow()

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

    fun refreshCartFromOutside() {
        refreshCart()
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
            _checkoutState.emit(NetworkResult.Loading())

            val result = checkoutUseCase(request)
            _checkoutState.emit(result)
        }
    }

    fun saveToReOrderHistory() {
        viewModelScope.launch {
            val entities = cartItems.value.map { item ->
                ReorderEntity(
                    productId = item.productId,
                    name = item.name,
                    imageUrl = item.imageUrl,
                    weight = item.weight,
                    price = item.price,
                    qty = item.qty,
                    categoryName = _products.value
                        .firstOrNull { it.id == item.productId }
                        ?.categoryName ?: "Others",
                    expiryDate = null,
                    inDate = null
                )
            }
            saveReorderHistoryUseCase(entities)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase.invoke()   // you must add this in usecase/repository
            refreshCart()
        }
    }

    fun getQty(productId: Long): StateFlow<Int> {
        return cartItems
            .map { list ->
                list.firstOrNull { it.productId == productId }?.qty ?: 0
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )
    }

}