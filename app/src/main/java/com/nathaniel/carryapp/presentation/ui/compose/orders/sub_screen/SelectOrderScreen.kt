package com.nathaniel.carryapp.presentation.ui.compose.orders.sub_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nathaniel.carryapp.R
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = spacing.sm,
                    end = spacing.sm,
                    top = spacing.md,
                    bottom = spacing.xl + 60.dp
                ),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Card view of the product here.
                items(products) { product -> // Use items to iterate over your product list
                    ProductCard(
                        dummyProduct = product,
                        viewModel = viewModel
                    )
                }
            }

            // Floating Icon Button (bottom-left)
            FloatingActionButton(
                onClick = { viewModel.onClickCart() }, // replace with your cart route
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White,
                shape = RoundedCornerShape(50),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 16.dp, end = 16.dp, bottom = 90.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart), // replace with your cart icon
                    contentDescription = "Go to Cart"
                )
            }
        }
    }
}