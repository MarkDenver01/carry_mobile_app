package com.nathaniel.carryapp.presentation.ui.compose.orders.category

import com.nathaniel.carryapp.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.domain.model.Category
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopBottomBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {

    // EXAMPLE categories
    val categories = listOf(
        Category("Chilled & Frozen", R.drawable.logo_final),
        Category("Fruits & Vegetables", R.drawable.logo_final),
        Category("Rice", R.drawable.logo_final),
        Category("Beverages", R.drawable.logo_final),
        Category("Snacks & Candies", R.drawable.logo_final),
        Category("Laundry & Cleaning", R.drawable.logo_final),
        Category("Cooking & Baking", R.drawable.logo_final),
        Category("Oats, Cereals, ...", R.drawable.logo_final),
        Category("Pasta & Noodles", R.drawable.logo_final),
        Category("Personal Care & Hygiene", R.drawable.logo_final),
        Category("Eggs & Milk", R.drawable.logo_final),
        Category("General Supplies", R.drawable.logo_final),
        Category("Alcohol", R.drawable.logo_final),
        Category("Baby Needs", R.drawable.logo_final),
        Category("Bread & Spread", R.drawable.logo_final),
        Category("Canned Goods", R.drawable.logo_final)
    )

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Scaffold(
        topBar = { ShopHeader(notifications = 12, cartCount = 15) },
        bottomBar = {
            ShopBottomBar(
                onHome = { navController.navigate(Routes.ORDERS) },
                onCategories = {},
                onReorder = { viewModel.onReorderClick() },
                onAccount = { viewModel.onAccountClick() }
            )
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // ----------------------------
            // SEARCH BAR (aligned same as OrderScreen)
            // ----------------------------
            ShopSearchBar(
                hint = "I'm looking for…",
                onSearch = {}
            )

            Spacer(Modifier.height(10.dp))

            // ----------------------------
            // HEADER
            // ----------------------------
            Text(
                text = "All Categories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E1F22),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            // ----------------------------
            // GRID — WITH SAFE HEIGHT
            // ----------------------------
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = screenHeight * 2), // ⭐ safe, prevents crash
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
                        imageRes = category.imageRes,
                        onClick = {
                            // TODO: Navigate to category list
                        }
                    )
                }
            }
        }
    }
}
