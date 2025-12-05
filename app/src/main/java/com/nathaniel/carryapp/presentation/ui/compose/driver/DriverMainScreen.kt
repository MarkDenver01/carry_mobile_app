package com.nathaniel.carryapp.presentation.ui.compose.driver

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverMainScreen(
    navController: NavController,
) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val cartViewModel: CartViewModel = sharedViewModel()
    val products by orderViewModel.products.collectAsState()
    val error by orderViewModel.error.collectAsState()
    val cartCount by cartViewModel.cartCount.collectAsState()
    val customerSession by orderViewModel.customerSession.collectAsState()
    // Convert from domain → UI
    val shopProducts = products.map { it.toShopProduct() }

    var selectedIndex by remember { mutableStateOf(0) }

    LaunchedEffect(products) {
        cartViewModel.setProducts(products)
    }

    LaunchedEffect(orderViewModel.loginUiAction) {
        orderViewModel.loginUiAction.collect { action ->
            when (action) {
                is LoginUiAction.Navigate -> {
                    navController.navigate(action.route) {
                        popUpTo(Routes.SIGN_IN) { inclusive = false }
                    }
                    orderViewModel.resetLoginAction()
                }

                is LoginUiAction.ShowToast -> "Order"
                null -> Unit
            }
        }
    }

    // ⚠ Handle API errors
    error?.let {
        Text("Error loading products: $it", color = Color.Red)
    }

    // Dynamic category groups
    val racks = shopProducts
        .groupBy { it.categoryName } // GROUP BY CATEGORY
        .map { (categoryName, productList) ->
            ProductRack(
                title = categoryName,
                products = productList
            )
        }

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
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // 🔍 Search + Banner
            item {
                Column {
                    ShopSearchBar(
                        hint = "I'm Smart Search AI, looking for…",
                        onSearch = { query ->
                            val customerId = customerSession?.customer?.customerId
                            if (customerId != null && query.isNotBlank()) {
                                orderViewModel.recordUserInteraction(customerId, query)
                                Timber.d("🔍 Search recorded: $query")
                            }
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    PromoBanner(
                        banners = listOf(
                            BannerItem(R.drawable.banner_wrap_n_carry),
                            BannerItem(R.drawable.banner_wrap_n_carry),
                            BannerItem(R.drawable.banner_wrap_n_carry)
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }

            // 🧩 Dynamic product racks
            items(racks.size) { index ->
                val rack = racks[index]

                // Header
                SectionHeader(
                    title = rack.title,
                    actionText = "View More",
                    onActionClick = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnViewMoreClicked) }
                )

                // Product grid per rack
                Spacer(Modifier.height(8.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.heightIn(max = 460.dp) // limit height for each rack
                ) {
                    items(rack.products, key = { it.id }) { p ->
                        ProductCard(
                            imageUrl = p.imageUrl,
                            name = p.name,
                            weight = p.weight,
                            sold = p.sold,
                            price = p.price,
                            onFavorite = {},
                            onAdd = {
                                cartViewModel.addProductOriginalDomain(p.id)

                                // ✅ Record user interaction when product is added
                                val customerId = customerSession?.customer?.customerId
                                if (customerId != null) {
                                    orderViewModel.recordUserInteraction(customerId, p.name)
                                    Timber.d("🛒 Recorded add-to-cart interaction for: ${p.name}")
                                }
                            },
                            onMinus = {
                                cartViewModel.removeProductOriginalDomain(p.id)
                            },
                            onDetailClick = {
                                // ✅ Optionally record when a product detail is viewed
                                val customerId = customerSession?.customer?.customerId
                                if (customerId != null) {
                                    orderViewModel.recordUserInteraction(customerId, p.name)
                                    Timber.d("👀 Recorded product view for: ${p.name}")
                                }

                                navController.navigate("${Routes.PRODUCT_DETAIL}/${p.id}")
                            }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
