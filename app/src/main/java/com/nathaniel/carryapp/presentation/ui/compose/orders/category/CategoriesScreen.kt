package com.nathaniel.carryapp.presentation.ui.compose.orders.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nathaniel.carryapp.domain.mapper.ProductMapper.toShopProduct
import com.nathaniel.carryapp.domain.model.Category
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopBottomBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopSearchBar
import com.nathaniel.carryapp.presentation.ui.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController
) {
    val orderViewModel: OrderViewModel = sharedViewModel()
    val products by orderViewModel.products.collectAsState()

    // Convert DOMAIN → UI
    val shopProducts = products.map { it.toShopProduct() }

    var selectedIndex by remember { mutableStateOf(0) }

    // -------------------------------------
    // BUILD CATEGORIES FROM REAL PRODUCT DATA
    // -------------------------------------
    val categories: List<Category> = shopProducts
        .groupBy { it.categoryName }
        .map { (categoryName, items) ->
            Category(
                name = categoryName,
                imageUrl = items.firstOrNull()?.imageUrl
            )
        }
        .sortedBy { it.name }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Scaffold(
        topBar = {
            ShopHeader(
                notifications = 12,
                cartCount = 15,
                onCartClick = {
                    navController.navigate(Routes.CART) {
                        popUpTo(Routes.ORDERS) { inclusive = true }
                    }
                },
                onNotificationClick = {}
            )
        },
        bottomBar = {
            ShopBottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it },
                onHome = { navController.navigate("home") },
                onCategories = { navController.navigate("categories") },
                onReorder = { navController.navigate("reorder") },
                onAccount = { navController.navigate("account") }
            )
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            ShopSearchBar(
                hint = "I'm looking for…",
                onSearch = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "All Categories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E1F22),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = screenHeight * 2),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 96.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(categories) { category ->
                    CategoryCard(
                        title = category.name,
                        imageUrl = category.imageUrl,
                        onClick = {
                            navController.navigate("${Routes.SORT_PRODUCT_BY_CATEGORY}/${category.name}") {
                                popUpTo(Routes.CATEGORIES) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally // <-- CENTER CONTENT
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            textAlign = TextAlign.Center,        // <-- CENTER TEXT
            modifier = Modifier.fillMaxWidth()   // <-- REQUIRED FOR TEXTALIGN
        )
    }
}
