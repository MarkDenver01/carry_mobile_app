package com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.model.ProductRack
import com.nathaniel.carryapp.presentation.ui.compose.orders.model.toShopProduct
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val products by viewModel.products.collectAsState()
    val error by viewModel.error.collectAsState()
    // Convert from domain → UI
    val shopProducts = products.map { it.toShopProduct() }

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route) {
                popUpTo(Routes.ORDERS) { inclusive = false }
            }
            viewModel.resetNavigation()
        }
    }

    // ⚠ Handle API errors
    error?.let {
        Text("Error loading products: $it", color = Color.Red)
    }

    // Dynamic category groups
    val racks = shopProducts
        .groupBy { it.category } // GROUP BY CATEGORY
        .map { (categoryName, productList) ->
            ProductRack(
                title = categoryName,
                products = productList
            )
        }

    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = { ShopHeader(notifications = 12, cartCount = 15) },
        bottomBar = {
            ShopBottomBar(
                onHome = { viewModel.onHomeClick() },
                onCategories = { viewModel.onCategoriesClick() },
                onReorder = { viewModel.onReorderClick() },
                onAccount = { viewModel.onAccountClick() }
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
                        hint = "I'm looking for…",
                        onSearch = { viewModel.onSearchClick() }
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
                    onActionClick = { viewModel.onViewMoreClick() }
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
                            onAdd = {},
                            onMinus = {},
                            onDetailClick = {
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
