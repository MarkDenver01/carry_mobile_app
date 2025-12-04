package com.nathaniel.carryapp.presentation.ui.compose.orders.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopBottomBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopSearchBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ProductCard
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.domain.model.ShopProduct
import com.nathaniel.carryapp.domain.mapper.ProductMapper.toShopProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilteredProductScreen(
    navController: NavController,
    categoryName: String
) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val cartViewModel: CartViewModel = sharedViewModel()
    var selectedIndex by remember { mutableStateOf(0) }

    // Set selected category
    LaunchedEffect(Unit) { orderViewModel.selectCategory(categoryName) }

    val products by orderViewModel.products.collectAsState()
    val selectedCategory by orderViewModel.selectedCategory.collectAsState()

    // Filter logic + disabled others
    val filtered = products
        .map { it.toShopProduct() }
        .map { shop ->
            shop.copy(
                enabled = shop.categoryName == selectedCategory
            )
        }

    Scaffold(
        topBar = {
            ShopHeader(
                notifications = 12,
                cartCount = cartViewModel.cartCount.collectAsState().value,
                onCartClick = { navController.navigate(Routes.CART) },
                onNotificationClick = {}
            )
        },
        bottomBar = {
            ShopBottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it },
                onHome = { orderViewModel.onHomeClick() },
                onCategories = { orderViewModel.onCategoriesClick() },
                onReorder = { orderViewModel.onReorderClick() },
                onAccount = { orderViewModel.onAccountClick() }
            )
        },
        containerColor = Color.White
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                ShopSearchBar(
                    hint = "Search inside $categoryName…",
                    onSearch = {}
                )
                Spacer(Modifier.height(12.dp))
            }

            item {
                Text(
                    text = "$categoryName Products",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(12.dp))
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.heightIn(max = 900.dp)
                ) {
                    items(filtered, key = { it.id }) { p ->
                        ProductCardFiltered(
                            product = p,
                            onAdd = {
                                if (p.enabled) cartViewModel.addProductOriginalDomain(p.id)
                            },
                            onMinus = {
                                if (p.enabled) cartViewModel.removeProductOriginalDomain(p.id)
                            },
                            onDetail = {
                                if (p.enabled) {
                                    navController.navigate("${Routes.PRODUCT_DETAIL}/${p.id}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCardFiltered(
    product: ShopProduct,
    onAdd: () -> Unit,
    onMinus: () -> Unit,
    onDetail: () -> Unit
) {
    val alpha = if (product.enabled) 1f else 0.4f

    Box(modifier = Modifier.alpha(alpha)) {
        ProductCard(
            imageUrl = product.imageUrl,
            name = product.name,
            weight = product.weight,
            sold = product.sold,
            price = product.price,
            onFavorite = {},
            onAdd = onAdd,
            onMinus = onMinus,
            onDetailClick = onDetail
        )
    }
}
