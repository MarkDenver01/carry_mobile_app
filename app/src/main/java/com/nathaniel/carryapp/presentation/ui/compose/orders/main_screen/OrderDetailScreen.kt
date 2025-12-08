package com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nathaniel.carryapp.domain.enum.OrderStatus
import com.nathaniel.carryapp.domain.response.OrderItemResponse
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.InfoRow
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.SectionCard
import com.nathaniel.carryapp.presentation.ui.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderId: Long?
) {
    val viewModel: OrderViewModel = sharedViewModel()
    val orders by viewModel.orders.collectAsState()

    val order = orders.firstOrNull { it.orderId == orderId }

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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(
                        text = "Order Detail",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        containerColor = Color(0xFFF7F8FA)
    ) { padding ->

        if (order == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Order not found")
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // ✅ ORDER HEADER
            item {
                SectionCard(title = "Order #${order.orderId}") {
                    Text("Placed on ${order.createdAt}", color = Color(0xFF6F7F85))
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "₱${"%,.2f".format(order.totalAmount)}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(18.dp))
            }

            // ✅ STATUS TRACKER
            item {
                SectionCard(title = "Order Status") {
                    OrderTrackerStepper(order.status)
                }
                Spacer(Modifier.height(18.dp))
            }

            // ✅ DELIVERY DETAILS
            item {
                SectionCard(title = "Delivery Information") {
                    InfoRow("Address", order.deliveryAddress)
                    InfoRow("Rider", order.riderName ?: "Not yet assigned")
                }
                Spacer(Modifier.height(18.dp))
            }

            // ✅ ITEMS
            item {
                SectionCard(title = "Order Items") {
                    order.items.forEach {
                        OrderItemRow(it)
                    }
                }
                Spacer(Modifier.height(18.dp))
            }

            // ✅ PAYMENT SUMMARY
            item {
                SectionCard(title = "Payment Summary") {
                    SummaryRow("Subtotal", order.subtotal)
                    SummaryRow("Delivery Fee", order.deliveryFee)
                    SummaryRow("Discount", order.discount)
                    Divider()
                    SummaryRow("Total", order.totalAmount, true)
                }
            }
        }
    }
}

@Composable
fun OrderTrackerStepper(status: OrderStatus) {
    val steps = listOf(
        OrderStatus.PENDING,
        OrderStatus.PROCESSING,
        OrderStatus.ON_DELIVERY,
        OrderStatus.DELIVERED,
        OrderStatus.CANCELLED
    )

    Column {
        steps.forEachIndexed { index, step ->

            val isCancelled = status == OrderStatus.CANCELLED
            val isActive = if (isCancelled) {
                step == OrderStatus.CANCELLED
            } else {
                step.ordinal <= status.ordinal
            }

            val color = when {
                isCancelled && step == OrderStatus.CANCELLED -> Color(0xFFF44336) // 🔴 RED
                isActive -> Color(0xFF118B3C) // 🟢 GREEN
                else -> Color(0xFFDADADA) // ⚪ GRAY
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                )

                if (index != steps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(26.dp)
                            .width(2.dp)
                            .background(color)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = step.name
                        .replace("_", " ")
                        .lowercase()
                        .replaceFirstChar { it.uppercase() },
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = color
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItemResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(item.productName, fontWeight = FontWeight.Medium)
            Text("Qty: ${item.quantity}", fontSize = 13.sp, color = Color.Gray)
        }

        Text(
            "₱${"%,.2f".format(item.lineTotal)}",
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SummaryRow(label: String, value: Double, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(
            "₱${"%,.2f".format(value)}",
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isTotal) 18.sp else 14.sp
        )
    }

    Spacer(Modifier.height(8.dp))
}



