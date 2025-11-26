package com.nathaniel.carryapp.presentation.ui.compose.orders.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController
) {
    val cartViewModel: CartViewModel = sharedViewModel()
    val cartItems by cartViewModel.cartItems.collectAsState()

    var voucherCode by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf("COD") }

    val total = cartItems.sumOf { it.subtotal }

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
        containerColor = Color(0xFFF7F8FA)
    ) { inner ->

        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {

            // ===========================
            // CART ITEMS SUMMARY
            // ===========================
            Text(
                "Cart",
                modifier = Modifier.padding(16.dp, 10.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            cartItems.forEach { item ->
                CheckoutItemCard(item)
            }

            Spacer(Modifier.height(12.dp))

            // ===========================
            // VOUCHER SECTION
            // ===========================
            Text(
                "Voucher Code",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedTextField(
                    value = voucherCode,
                    onValueChange = { voucherCode = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter voucher code") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = { /* apply voucher logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C)),
                    modifier = Modifier.height(55.dp)
                ) {
                    Text("Apply")
                }
            }

            Spacer(Modifier.height(8.dp))

            // ===========================
            // PAYMENT METHOD
            // ===========================
            Text(
                "Payment Method",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Column(Modifier.padding(16.dp)) {

                PaymentOption(
                    title = "Online Payment",
                    sub = "(GCash / Maya)",
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

            Spacer(Modifier.height(18.dp))

            // ===========================
            // BOTTOM CONFIRM BUTTON
            // ===========================
            Column(
                Modifier
                    .fillMaxWidth()
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
                        // TODO: Continue to place order
                        // navController.navigate("order_success")
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
    }
}

@Composable
fun CheckoutItemCard(item: com.nathaniel.carryapp.domain.model.CartDisplayItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(item.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(item.weight, color = Color.Gray)

                Spacer(Modifier.height(6.dp))

                Text(
                    "₱${"%,.2f".format(item.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF118B3C)
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Text("${item.qty}", fontWeight = FontWeight.Bold)
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