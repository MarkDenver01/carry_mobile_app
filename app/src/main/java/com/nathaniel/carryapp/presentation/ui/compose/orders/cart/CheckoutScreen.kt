package com.nathaniel.carryapp.presentation.ui.compose.orders.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nathaniel.carryapp.domain.model.CartDisplayItem
import com.nathaniel.carryapp.domain.request.CheckoutItemRequest
import com.nathaniel.carryapp.domain.request.CheckoutRequest
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.CustomerViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.utils.NetworkResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController
) {
    val cartViewModel: CartViewModel = sharedViewModel()
    val customerViewModel: CustomerViewModel = sharedViewModel()
    val orderViewModel: OrderViewModel = sharedViewModel()
    val cartItems by cartViewModel.cartItems.collectAsState()

    var voucherCode by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf("COD") }
    val walletBalance by customerViewModel.walletBalance.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    val total = cartItems.sumOf { it.subtotal }

    // Observe result
    LaunchedEffect(Unit) {
        cartViewModel.checkoutState.collect { result ->
            when (result) {

                is NetworkResult.Loading -> {
                    // TODO: Add loader if you want
                }

                is NetworkResult.Success -> {
                    // Wallet deduction
                    if (selectedPayment == "EWallet") {
                        customerViewModel.refreshWallet()
                    }

                    // Clear cart after successful checkout
                    cartViewModel.clearCart()

                    // Navigate to success screen
                    navController.navigate(Routes.ORDERS) {
                        popUpTo(Routes.CHECKOUT) { inclusive = true }
                    }
                }

                is NetworkResult.Error -> {
                    // TODO: Show error dialog/toast
                }

                else -> Unit
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF118B3C),
                    titleContentColor = Color.White
                ),
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color(0xFFF7F8FA),

        // ⭐ FIXED CONFIRM BUTTON AT BOTTOM
        bottomBar = {
            Column(
                Modifier
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", fontSize = 18.sp)
                    Text(
                        "₱${"%,.2f".format(total)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF118B3C)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        showConfirmDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Confirm",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    ) { inner ->

        // ⭐ EVERYTHING SCROLLABLE NOW
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {

            // CART TITLE
            item {
                Text(
                    "Cart",
                    modifier = Modifier.padding(16.dp, 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // EACH ITEM
            items(cartItems.size) { index ->
                CheckoutItemCard(cartItems[index])
            }

            item { Spacer(Modifier.height(16.dp)) }

            // PAYMENT METHOD TITLE
            item {
                Text(
                    "Payment Method",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // PAYMENT OPTIONS
            item {
                Column(Modifier.padding(16.dp)) {

                    PaymentOption(
                        title = "₱${"%,.2f".format(walletBalance)}",
                        sub = "Wallet Balance",
                        isSelected = selectedPayment == "online",
                        onClick = { selectedPayment = "online" }
                    )

                    Spacer(Modifier.height(10.dp))

                    PaymentOption(
                        title = "Cash on Delivery",
                        isSelected = selectedPayment == "COD",
                        onClick = { selectedPayment = "COD" }
                    )
                }
            }

            item { Spacer(Modifier.height(100.dp)) } // SPACE ABOVE BOTTOM BUTTON
        }
    }

    // ⭐ BOTTOM CONFIRMATION POPUP
    if (showConfirmDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    showConfirmDialog = false
                },
            contentAlignment = Alignment.BottomCenter
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "Are you sure you want to\ncheckout your order?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showConfirmDialog = false

                            val customerId =
                                orderViewModel.customerSession.value?.customer?.customerId
                            val deliveryAddress =
                                orderViewModel.reverseAddress.value.fullAddressLine
                            val notes: String? = null

                            if (customerId == null) {
                                // TODO: Show error dialog
                                return@Button
                            }

                            // Build checkout items from cart
                            val checkoutItems = cartItems.map {
                                CheckoutItemRequest(
                                    productId = it.productId,
                                    quantity = it.qty
                                )
                            }

                            val backendPayment = if (selectedPayment == "COD") "COD" else "WALLET"

                            val request = CheckoutRequest(
                                customerId = customerId,
                                paymentMethod = backendPayment,
                                deliveryFee = 0.0,
                                discount = 0.0,
                                deliveryAddress = deliveryAddress ?: "",
                                notes = notes,
                                items = checkoutItems
                            )

                            cartViewModel.checkout(request)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Complete Orders", fontSize = 18.sp, color = Color.White)
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        modifier = Modifier.clickable {
                            showConfirmDialog = false
                        }
                    )
                }
            }
        }
    }

}


@Composable
fun CheckoutItemCard(
    item: CartDisplayItem
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
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

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0E1F22)
                )

                Text(
                    text = item.weight,
                    fontSize = 14.sp,
                    color = Color(0xFF6F7F85)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "₱${"%,.2f".format(item.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF118B3C)
                )
            }

            // Qty Box
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0F1F3)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${item.qty}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Composable
fun PaymentOption(
    title: String,
    sub: String = "",
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFF118B3C) else Color.Transparent)
                .border(
                    width = 2.dp,
                    color = if (isSelected) Color(0xFF118B3C) else Color.Gray,
                    shape = CircleShape
                )
        )

        Spacer(Modifier.width(14.dp))

        Column {
            Text(title, fontSize = 17.sp, fontWeight = FontWeight.Medium)
            if (sub.isNotEmpty()) Text(sub, fontSize = 13.sp, color = Color.Gray)
        }
    }
}