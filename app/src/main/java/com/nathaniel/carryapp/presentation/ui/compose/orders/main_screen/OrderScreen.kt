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
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.*

data class ShopProduct(
    val id: String,
    val name: String,
    val weight: String,
    val sold: String,
    val price: String,
    val imageRes: Int
)

data class ProductRack(
    val title: String,
    val products: List<ShopProduct>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    // 🧩 Mock products (sample, replace with real VM data)
    val sampleProducts = remember {
        listOf(
            ShopProduct("1", "Carrots Regular", "230–250G", "15.3k", "₱45", R.drawable.logs),
            ShopProduct("2", "Banana Lakatan Ripe", "0.9–1KG", "13.7k", "₱115", R.drawable.logs),
            ShopProduct("3", "Cabbage", "450–500G", "15.1k", "₱38", R.drawable.logs),
            ShopProduct("4", "Tomato", "300–350G", "9.6k", "₱28", R.drawable.logs)
        )
    }

    // 🗂️ All racks (categories)
    val racks = remember {
        listOf(
            ProductRack("Beverages", sampleProducts),
            ProductRack("Wines & Liquor", sampleProducts),
            ProductRack("Snacks", sampleProducts),
            ProductRack("Sweets", sampleProducts),
            ProductRack("Milk Products", sampleProducts),
            ProductRack("Formula Milk & Baby Foods", sampleProducts),
            ProductRack("Cigars", sampleProducts),
            ProductRack("Condiments, Sauces, & Dressings", sampleProducts),
            ProductRack("Canned Goods", sampleProducts),
            ProductRack("Grocery Staples", sampleProducts)
        )
    }

    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = { ShopHeader(notifications = 12, cartCount = 15) },
        bottomBar = {
            ShopBottomBar(
                onHome = { viewModel.onLoginClick() },
                onCategories = { viewModel.onLoginClick() },
                onReorder = { viewModel.onLoginClick() },
                onAccount = { viewModel.onLoginClick() }
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
                        onSearch = { viewModel.onLoginClick() }
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
                    onActionClick = { viewModel.onLoginClick() }
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
                            imageRes = p.imageRes,
                            name = p.name,
                            weight = p.weight,
                            sold = p.sold,
                            price = p.price,
                            onFavorite = {},
                            onAdd = {},
                            onMinus = {}
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
