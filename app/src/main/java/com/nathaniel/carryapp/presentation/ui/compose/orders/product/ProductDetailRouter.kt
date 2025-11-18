package com.nathaniel.carryapp.presentation.ui.compose.orders.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel

@Composable
fun ProductDetailRouter(
    navController: NavController,
    productId: Long?,
    viewModel: OrderViewModel = hiltViewModel()
) {
    // Get current product list already loaded on OrderViewModel init
    val products = viewModel.products.collectAsState().value

    // Find product
    val product = products.firstOrNull { it.id == productId }

    if (product != null) {
        ProductDetailScreen(
            imageUrl = product.imageUrl,
            name = product.name,
            size = product.size,
            description = product.description,
            price = "₱${product.price}",
            onBack = { navController.popBackStack() }
        )
    } else {
        // Optional fallback UI
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Product not found",
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}