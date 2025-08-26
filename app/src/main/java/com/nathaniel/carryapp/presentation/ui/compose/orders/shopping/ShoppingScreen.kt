package com.nathaniel.carryapp.presentation.ui.compose.orders.shopping

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.navigation.BottomNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.CartItemCard
import com.nathaniel.carryapp.presentation.utils.DynamicButton

data class DummyCartItem(
    val name: String,
    val price: Double,
    val stock: Int,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val cartItems = listOf(
        DummyCartItem(
            "Maharlika Super Special Rice (Whole Grain)",
            1650.00,
            20,
            R.drawable.ic_denorado
        ),
        DummyCartItem(
            "Similac Tumicare HW3+ Above 3 years old",
            2919.95,
            50,
            R.drawable.ic_denorado
        ),
        DummyCartItem("Century Tuna Hot & Spicy", 60.00, 105, R.drawable.ic_denorado)
    )

    val quantities = remember { mutableStateListOf(1, 1, 1) }

    val subtotal = cartItems.mapIndexed { index, item ->
        item.price * quantities[index]
    }.sum()

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
                title = "Cart",
                showBackButton = true,
                showMenuButton = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.md, vertical = spacing.sm)
        ) {
            cartItems.forEachIndexed { index, item ->
                CartItemCard(
                    item = item,
                    quantity = quantities[index],
                    onIncrease = {
                        if (quantities[index] < item.stock) quantities[index]++
                    },
                    onDecrease = {
                        if (quantities[index] > 1) quantities[index]--
                    },
                    onRemove = {
                        // Remove logic here if needed
                    },
                    onAddRecommended = {

                    },
                    onViewRecommended = {

                    }
                )

                Spacer(modifier = Modifier.height(spacing.sm))

            }

            // Subtotal and Checkout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(spacing.md),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${cartItems.size} item",
                    color = Color.Black
                )
                Text(
                    text = "Subtotal: ₱${"%,.2f".format(subtotal)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = sizes.titleFontSize
                )

                Spacer(modifier = Modifier.height(spacing.sm))

                DynamicButton(
                    onClick = { },
                    height = sizes.buttonHeight,
                    fontSize = sizes.buttonFontSize,
                    backgroundColor = Color(0xFF2E7D32),
                    content = "PROCEED TO CHECKOUT"
                )
            }
        }
    }
}

