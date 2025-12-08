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
import com.nathaniel.carryapp.domain.enum.AlertType
import com.nathaniel.carryapp.domain.model.CartDisplayItem
import com.nathaniel.carryapp.domain.model.MembershipResponse
import com.nathaniel.carryapp.domain.request.CheckoutItemRequest
import com.nathaniel.carryapp.domain.request.CheckoutRequest
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.CustomerViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import com.nathaniel.carryapp.presentation.utils.SweetAlertDialog
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController
) {
    val cartViewModel: CartViewModel = sharedViewModel()
    val customerViewModel: CustomerViewModel = sharedViewModel()
    val orderViewModel: OrderViewModel = sharedViewModel()
    val membership by orderViewModel.membership.collectAsState()
    val customerSession by orderViewModel.customerSession.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val walletBalance by customerViewModel.walletBalance.collectAsState()

    var selectedPayment by remember { mutableStateOf("COD") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showDialogErrorPayment by remember { mutableStateOf(false) }
    var showDialogSuccessPayment by remember { mutableStateOf(false) }
    var showUsePointsDialog by remember { mutableStateOf(false) }
    var usePoints by remember { mutableStateOf(false) }

    val total = cartItems.sumOf { it.subtotal }

    // ✅ OBSERVE CHECKOUT RESULT
    LaunchedEffect(true) {
        cartViewModel.checkoutState.collect { result ->
            when (result) {
                is NetworkResult.Loading -> Unit
                is NetworkResult.Idle -> Unit

                is NetworkResult.Success -> {
                    showDialogSuccessPayment = true
                }

                is NetworkResult.Error -> {
                    showDialogErrorPayment = true
                }
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

        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {

            item {
                Text(
                    "Cart",
                    modifier = Modifier.padding(16.dp, 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(cartItems.size) { index ->
                CheckoutItemCard(cartItems[index])
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Text(
                    "Payment Method",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(Modifier.padding(16.dp)) {

                    PaymentOption(
                        title = "₱${"%,.2f".format(walletBalance)}",
                        sub = "Wallet Balance",
                        isSelected = selectedPayment == "WALLET",
                        onClick = { selectedPayment = "WALLET" }
                    )

                    Spacer(Modifier.height(10.dp))

                    PaymentOption(
                        title = "Cash on Delivery",
                        isSelected = selectedPayment == "COD",
                        onClick = { selectedPayment = "COD" }
                    )
                }
            }

            item { Spacer(Modifier.height(100.dp)) }
        }
    }

    // ✅ ✅ ✅ SUCCESS DIALOG — WALANG LOCAL WALLET DEDUCT
    if (showDialogSuccessPayment) {
        SweetAlertDialog(
            type = AlertType.SUCCESS,
            title = "Payment Success",
            message = "Payment has been successful. Will be notified you regarding your order.",
            show = showDialogSuccessPayment,
            confirmText = "Close",
            isSingleButton = true,
            onConfirm = {
                showDialogSuccessPayment = false

                // ✅ Add membership points
                val customerId =
                    orderViewModel.customerSession.value?.customer?.customerId

                if (customerId != null) {
                    orderViewModel.addPointsAfterPurchase(
                        customerId = customerId,
                        totalAmount = total   // ✅ eto ang base ng 500 pesos computation
                    )
                }

                // ✅ SAVE REORDER
                cartViewModel.saveToReOrderHistory()

                // ✅ CLEAR CART
                cartViewModel.clearCart()

                // ✅ REFRESH WALLET FROM BACKEND
                customerViewModel.refreshWallet()

                // ✅ NAVIGATE
                navController.navigate(Routes.ORDERS) {
                    popUpTo(Routes.CHECKOUT) { inclusive = true }
                }
            }
        )
    }

    // ✅ ERROR DIALOG
    if (showDialogErrorPayment) {
        SweetAlertDialog(
            type = AlertType.ERROR,
            title = "Error",
            message = "There is an unexpected error occur during the payment.",
            show = showDialogErrorPayment,
            confirmText = "Close",
            isSingleButton = true,
            onConfirm = {
                showDialogErrorPayment = false
            }
        )
    }

    // ✅ CONFIRMATION POPUP
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

                            val membership = orderViewModel.membership.value

                            val deliveryAddress =
                                orderViewModel.reverseAddress.value.fullAddressLine

                            if (customerId == null) return@Button

                            // ✅ Frontend wallet validation (UI only)
                            if (selectedPayment == "WALLET" && walletBalance < total) {
                                println("Insufficient Wallet Balance")
                                return@Button
                            }

                            val checkoutItems = cartItems.map {
                                CheckoutItemRequest(
                                    productId = it.productId,
                                    quantity = it.qty
                                )
                            }

                            val backendPayment =
                                if (selectedPayment == "COD") "COD" else "WALLET"

                            val request = CheckoutRequest(
                                customerId = customerId,
                                paymentMethod = backendPayment,
                                deliveryFee = 0.0,
                                discount = 0.0,
                                deliveryAddress = deliveryAddress ?: "",
                                notes = null,
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

    if (showUsePointsDialog) {
        UsePointsDialog(
            membership = membership,
            total = total,
            customerId = customerSession?.customer?.customerId,
            orderViewModel = orderViewModel,
            cartViewModel = cartViewModel,
            onClose = { showUsePointsDialog = false }
        )
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

@Composable
fun UsePointsDialog(
    membership: MembershipResponse?,
    total: Double,
    customerId: Long?,
    orderViewModel: OrderViewModel,
    cartViewModel: CartViewModel,
    onClose: () -> Unit
) {
    if (membership == null || customerId == null) return

    var usePoints by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Use Points?") },
        text = {
            Column {
                Text("You have ${membership.pointsBalance} points")

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = usePoints,
                        onClick = { usePoints = true }
                    )
                    Text("Use my points")
                }
            }
        },
        confirmButton = {
            Button(onClick = {

                val discount = if (usePoints)
                    orderViewModel.computeDiscount(membership.pointsBalance)
                else 0.0

                val finalRequest = CheckoutRequest(
                    customerId = customerId,
                    paymentMethod = "WALLET",
                    deliveryFee = 0.0,
                    discount = discount,
                    deliveryAddress = "",
                    notes = null,
                    items = cartViewModel.cartItems.value.map {
                        CheckoutItemRequest(it.productId, it.qty)
                    }
                )

                cartViewModel.checkout(finalRequest)

                // ✅ DEDUCT POINTS
                if (usePoints) {
                    val pointsToDeduct = (discount / 100 * 1000).toInt()
                    orderViewModel.deductPoints(customerId, pointsToDeduct)
                }

                onClose()
            }) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(onClick = {
                val normalRequest = CheckoutRequest(
                    customerId = customerId,
                    paymentMethod = "WALLET",
                    deliveryFee = 0.0,
                    discount = 0.0,
                    deliveryAddress = "",
                    notes = null,
                    items = cartViewModel.cartItems.value.map {
                        CheckoutItemRequest(it.productId, it.qty)
                    }
                )

                cartViewModel.checkout(normalRequest)
                onClose()
            }) {
                Text("No")
            }
        }
    )
}
