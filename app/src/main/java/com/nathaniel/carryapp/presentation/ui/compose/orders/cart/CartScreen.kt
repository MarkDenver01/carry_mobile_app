package com.nathaniel.carryapp.presentation.ui.compose.orders.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nathaniel.carryapp.domain.model.CartDisplayItem
import com.nathaniel.carryapp.navigation.Routes
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
                    containerColor = Color(0xFF118B3C),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
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
        containerColor = Color(0xFFF7F8FA),
        bottomBar = {
            CheckoutBottomBar(
                total = total,
                onClick = {
                    navController.navigate(Routes.CHECKOUT) {
                        popUpTo(Routes.CATEGORIES) { inclusive = true }
                    }
                }
            )
        }
    ) { inner ->

        if (cartItems.isEmpty()) {
            EmptyCartState(Modifier.padding(inner))
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize()
            ) {

                // Header
                item {
                    CartHeaderRow(
                        itemCount = itemsCount,
                        onClear = {
                            cartItems.forEach { item ->
                                repeat(item.qty) {
                                    cartViewModel.removeProductOriginalDomain(item.productId)
                                }
                            }
                        }
                    )
                }

                item { Spacer(Modifier.height(4.dp)) }

                // EACH ITEM CARD
                items(cartItems.size) { index ->
                    val item = cartItems[index]

                    CartItemCard(
                        item = item,
                        onIncrement = { cartViewModel.addProductOriginalDomain(item.productId) },
                        onDecrement = { cartViewModel.removeProductOriginalDomain(item.productId) },
                        onRemove = {
                            repeat(item.qty) {
                                cartViewModel.removeProductOriginalDomain(item.productId)
                            }
                        },
                        onRecommend = { selectedItem ->
                            // TODO: navigation to recommendation screen
                            //navController.navigate("recommend/${selectedItem.productId}")
                        }
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        AnimatedLoaderOverlay(isLoading)
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
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "$itemCount items",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0E1F22)
        )

        Text(
            text = "Clear all",
            color = Color(0xFFE74C3C),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onClear() }
        )
    }
}


@Composable
private fun CartItemCard(
    item: CartDisplayItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    onRecommend: (CartDisplayItem) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {

            // Product Image
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E1F22)
                )

                Text(
                    item.weight,
                    fontSize = 14.sp,
                    color = Color(0xFF6F7F85)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    "₱${"%,.2f".format(item.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF118B3C)
                )

                Spacer(Modifier.height(14.dp))

                // ⭐ EXACT SAME UI AS ORDER SCREEN (+ / - layout)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        // MINUS
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF0F1F3))
                                .clickable { onDecrement() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = item.qty.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.width(26.dp),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.width(8.dp))

                        // PLUS
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF0F1F3))
                                .clickable { onIncrement() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // REMOVE
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onRemove() }
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = null, tint = Color.Gray)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Remove",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                // ⭐ NEW — View Suggestions Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onRecommend(item) }
                    ) {
                        Text(
                            "View Suggestions →",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF118B3C)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun QuantityButton(symbol: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF0F1F3))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(symbol, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CheckoutBottomBar(
    total: Double,
    onClick: () -> Unit
) {
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

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { onClick() },
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(com.nathaniel.carryapp.R.raw.empty_cart)
        )

        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        // ⭐ Lottie animation
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(220.dp)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Your cart is empty",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1D1D1D)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            "You haven't added any products yet.\nStart shopping now!",
            fontSize = 14.sp,
            color = Color(0xFF75828A),
            textAlign = TextAlign.Center
        )
    }
}

