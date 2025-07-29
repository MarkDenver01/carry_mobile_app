package com.nathaniel.carryapp.presentation.ui.compose.orders.sub_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.navigation.BottomNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.ProductCard
import com.nathaniel.carryapp.presentation.utils.DynamicButton

data class DummyProduct(
    val id: Int,
    val productName: String,
    val productDescription: String,
    val imageUrl: String,
    val price: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectOderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    /// sample products
    val products = List(10) {
        DummyProduct(
            id = it,
            productName = "Product $it",
            productDescription = "Description for Product $it",
            imageUrl = "https://example.com/image$it.jpg",
            price = it * 10.0
        )
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
                )
            ),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "Product Category",
                showBackButton = false,
                showMenuButton = false,
                scrollBehavior = scrollBehavior
            )
        }, bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = spacing.sm,
                end = spacing.sm,
                top = spacing.md,
                bottom = spacing.xl + innerPadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Card view of the product here.
            items(products) { product -> // Use items to iterate over your product list
                ProductCard(product)
            }
        }
    }
}