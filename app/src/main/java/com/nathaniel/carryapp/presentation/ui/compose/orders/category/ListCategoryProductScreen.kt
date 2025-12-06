package com.nathaniel.carryapp.presentation.ui.compose.orders.category

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.mapper.ProductMapper.toShopProduct
import com.nathaniel.carryapp.domain.model.ProductRack
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.*
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.ui.state.LoginUiAction
import com.nathaniel.carryapp.presentation.ui.state.LoginUiEvent

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCategoryProductScreen(
    navController: NavController,
    categoryName: String,  // ← Category from OrderScreen
) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val cartViewModel: CartViewModel = sharedViewModel()

    val products by orderViewModel.products.collectAsState()
    val customerSession by orderViewModel.customerSession.collectAsState()
    val cartCount by cartViewModel.cartCount.collectAsState()
    val error by orderViewModel.error.collectAsState()

    val shopProducts = products.map { it.toShopProduct() }

    // Group by category
    val racks = shopProducts
        .groupBy { it.categoryName }
        .map { (cat, list) -> ProductRack(cat, list) }

    // ⭐ Selected category (auto from OrderScreen)
    var selectedCategory by remember { mutableStateOf(categoryName) }

    // ⭐ Filter products for the selected category
    val filteredProducts = remember(selectedCategory, racks) {
        racks.find { it.title == selectedCategory }?.products ?: emptyList()
    }

    LaunchedEffect(products) {
        cartViewModel.setProducts(products)
    }

    // Handle navigation actions
    LaunchedEffect(Unit) {
        orderViewModel.loginUiAction.collect { action ->
            when (action) {
                is LoginUiAction.Navigate -> {
                    navController.navigate(action.route) {
                        popUpTo(Routes.SIGN_IN) { inclusive = false }
                    }
                    orderViewModel.resetLoginAction()
                }

                else -> Unit
            }
        }
    }

    // -------------------------------------------------------
    // UI Layout
    // -------------------------------------------------------
    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = {
            ShopHeader(
                notifications = 12,
                cartCount = cartCount,
                onCartClick = { navController.navigate(Routes.CART) },
                onNotificationClick = {}
            )
        },
        bottomBar = {
            ShopBottomBar(
                selectedIndex = orderViewModel.selectedTab.collectAsState().value,
                onItemSelected = { orderViewModel.updateSelectedTab(it) },
                onHome = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnHomeClicked) },
                onCategories = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnCategoriesClicked) },
                onReorder = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnReorderClicked) },
                onAccount = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnAccountClicked) }
            )
        }
    ) { inner ->

        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {

            // Search
            ShopSearchBar(
                hint = "I'm Smart Search AI, looking for…",
                onSearch = { query ->
                    val customerId = customerSession?.customer?.customerId
                    if (customerId != null && query.isNotBlank()) {
                        orderViewModel.recordUserInteraction(customerId, query)
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            // Banner
            PromoBanner(
                banners = listOf(
                    BannerItem(R.drawable.banner_wrap_n_carry),
                    BannerItem(R.drawable.banner_wrap_n_carry),
                    BannerItem(R.drawable.banner_wrap_n_carry)
                )
            )

            Spacer(Modifier.height(12.dp))

            if (error != null) {
                Text("⚠ Error loading products: $error", color = Color.Red)
            }

            // ⭐ Final Display (left menu + grid)
            CategoryProduct(
                racks = listOf(ProductRack(selectedCategory, filteredProducts)), // filtered
                originalRacks = racks,                      // full category list
                selectedCategory = selectedCategory,         // highlight selected
                onCategorySelected = { selectedCategory = it },

                onProductClick = { id ->
                    navController.navigate("${Routes.PRODUCT_DETAIL}/$id")
                },
                onAdd = { id -> cartViewModel.addProductOriginalDomain(id) },
                onMinus = { id -> cartViewModel.removeProductOriginalDomain(id) }
            )
        }
    }
}
