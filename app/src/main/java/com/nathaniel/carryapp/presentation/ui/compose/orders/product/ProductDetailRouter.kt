package com.nathaniel.carryapp.presentation.ui.compose.orders.product

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.*
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailRouter(
    navController: NavController,
    productId: Long?
) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val cartViewModel: CartViewModel = sharedViewModel()
    val customerSession by orderViewModel.customerSession.collectAsState()

    val products by orderViewModel.products.collectAsState()
    val related by orderViewModel.related.collectAsState()

    val isProductsLoading by orderViewModel.isProductsLoading.collectAsState()
    val isRelatedLoading by orderViewModel.isRelatedLoading.collectAsState()

    // Load related products
    LaunchedEffect(productId) {
        if (productId != null) {
            orderViewModel.loadRelatedProducts(productId)
        }
    }

    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = {
            ShopHeader(
                notifications = 0,
                cartCount = cartViewModel.cartCount.collectAsState().value,
                onCartClick = { navController.navigate(Routes.CART) },
                onNotificationClick = {}
            )
        }
    ) { innerPadding ->

        // ----------------------------
        // LOADING MAIN PRODUCT
        // ----------------------------
        if (isProductsLoading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // ----------------------------
        // PRODUCT NOT FOUND
        // ----------------------------
        val product = products.firstOrNull { it.id == productId }
        if (product == null) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Product not found", color = Color.Red)
            }
            return@Scaffold
        }

        // ----------------------------
        // MAIN CONTENT
        // ----------------------------
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // --------------------------------
            // PRODUCT DETAIL TOP VIEW
            // --------------------------------
            item {
                ProductDetailScreen(
                    imageUrl = product.imageUrl,
                    name = product.name,
                    size = product.size,
                    description = product.productDescription,
                    price = "₱${product.price}",
                    onBack = { navController.popBackStack() }
                )
            }

            // --------------------------------
            // TITLE
            // --------------------------------
            item {
                Text(
                    text = "Product Recommended by Ka-Tropa",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // --------------------------------
            // RELATED LOADING
            // --------------------------------
            if (isRelatedLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(6.dp))
                            Text("Tropa is thinking of the best combos…")
                        }
                    }
                }
            }

            // --------------------------------
            // RELATED PRODUCTS
            // --------------------------------
            if (!isRelatedLoading && related.isNotEmpty()) {
                items(related) { p ->
                    ProductCard(
                        imageUrl = p.imageUrl,
                        name = p.name,
                        weight = p.size,
                        sold = 0,
                        price = p.price,
                        onFavorite = {},
                        onAdd = {
                            cartViewModel.addProductOriginalDomain(p.id)
                            val customerId = customerSession?.customer?.customerId
                            if (customerId != null) {
                                orderViewModel.recordUserInteraction(customerId, p.name)
                                Timber.d("🛒 Added: ${p.name}")
                            }
                        },
                        onMinus = {
                            cartViewModel.removeProductOriginalDomain(p.id)
                        },
                        onDeduct = { orderViewModel.deductStock(p.id) },
                        onRestore = { orderViewModel.restoreStock(p.id) },

                        onDetailClick = {
                            val customerId = customerSession?.customer?.customerId
                            if (customerId != null) {
                                orderViewModel.recordUserInteraction(customerId, p.name)
                            }
                            navController.navigate("${Routes.PRODUCT_DETAIL}/${p.id}")
                        }
                    )
                }
            }

            // --------------------------------
            // EMPTY STATE
            // --------------------------------
            if (!isRelatedLoading && related.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No recommendations yet.", color = Color.Gray)
                    }
                }
            }
        }
    }
}
