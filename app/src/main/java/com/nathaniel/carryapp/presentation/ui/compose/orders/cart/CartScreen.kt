package com.nathaniel.carryapp.presentation.ui.compose.orders.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.model.CartDisplayItem
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.utils.AnimatedLoaderOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController
) {
    val cartViewModel: CartViewModel = sharedViewModel()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val isLoading by cartViewModel.isLoading.collectAsState()

    val total = cartItems.sumOf { it.subtotal }
    val itemsCount = cartItems.sumOf { it.qty }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2F7D32),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "", tint = Color.White)
                    }
                },
                title = {
                    Text(
                        text = "My Cart ($itemsCount)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        containerColor = Color.White,
        bottomBar = {
            CheckoutBottomBar(total = total)
        }
    ) { inner ->

        Box(modifier = Modifier.fillMaxSize()) {

            if (cartItems.isEmpty()) {
                EmptyCartState(modifier = Modifier.padding(inner))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(inner)
                        .fillMaxSize()
                ) {

                    item {
                        CartHeaderRow(
                            itemCount = itemsCount,
                            onClear = {
                                // optional: pwede kang gumawa ng ClearCartUseCase
                                // for now loop remove all
                                cartItems.forEach { item ->
                                    repeat(item.qty) {
                                        cartViewModel.decrementProduct(
                                            product = com.nathaniel.carryapp.domain.model.Product(
                                                id = item.productId,
                                                name = item.name,
                                                code = "",
                                                size = item.weight,
                                                price = item.price,
                                                imageUrl = item.imageUrl,
                                                description = "",
                                                stocks = 0,
                                                category = ""
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }

                    items(cartItems.size) { index ->
                        val item = cartItems[index]
                        CartItemRow(
                            item = item,
                            onIncrement = {
                                cartViewModel.addProduct(
                                    product = com.nathaniel.carryapp.domain.model.Product(
                                        id = item.productId,
                                        name = item.name,
                                        code = "",
                                        size = item.weight,
                                        price = item.price,
                                        imageUrl = item.imageUrl,
                                        description = "",
                                        stocks = 0,
                                        category = ""
                                    )
                                )
                            },
                            onDecrement = {
                                cartViewModel.decrementProduct(
                                    product = com.nathaniel.carryapp.domain.model.Product(
                                        id = item.productId,
                                        name = item.name,
                                        code = "",
                                        size = item.weight,
                                        price = item.price,
                                        imageUrl = item.imageUrl,
                                        description = "",
                                        stocks = 0,
                                        category = ""
                                    )
                                )
                            },
                            onRemove = {
                                // remove lahat ng qty
                                repeat(item.qty) {
                                    cartViewModel.decrementProduct(
                                        product = com.nathaniel.carryapp.domain.model.Product(
                                            id = item.productId,
                                            name = item.name,
                                            code = "",
                                            size = item.weight,
                                            price = item.price,
                                            imageUrl = item.imageUrl,
                                            description = "",
                                            stocks = 0,
                                            category = ""
                                        )
                                    )
                                }
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            AnimatedLoaderOverlay(isLoading)
        }
    }
}

@Composable
private fun CartHeaderRow(
    itemCount: Int,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$itemCount items",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = "Clear all items",
            color = Color(0xFFE74C3C),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onClear() }
        )
    }
}

@Composable
private fun CartItemRow(
    item: CartDisplayItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ⭐ EXACT SAME IMAGE LOADING STYLE AS ORDER SCREEN ⭐
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D)
            )

            Text(
                text = item.weight,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "₱${"%,.0f".format(item.price)}",
                    color = Color(0xFF118B3C),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrement) {
                        Text("-", fontSize = 18.sp)
                    }
                    Text(
                        text = item.qty.toString(),
                        modifier = Modifier.width(24.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    IconButton(onClick = onIncrement) {
                        Text("+", fontSize = 18.sp)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onRemove() }
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = "", tint = Color.Gray)
                    Spacer(Modifier.width(4.dp))
                    Text("Remove", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}


@Composable
private fun CheckoutBottomBar(total: Double) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .navigationBarsPadding()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total", fontSize = 17.sp, color = Color.Black)
            Text("₱${"%,.2f".format(total)}", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = { /* TODO: place order */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                "Checkout",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun EmptyCartState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Your cart is empty.", color = Color.Gray)
    }
}
