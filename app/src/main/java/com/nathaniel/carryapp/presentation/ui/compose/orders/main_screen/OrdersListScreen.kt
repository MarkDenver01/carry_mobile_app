package com.nathaniel.carryapp.presentation.ui.compose.orders.main_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nathaniel.carryapp.domain.enum.OrderStatus
import com.nathaniel.carryapp.domain.response.CustomerOrderResponse
import com.nathaniel.carryapp.domain.response.OrderResponse
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListScreen(
    navController: NavController
) {
    val viewModel: OrderViewModel = sharedViewModel()
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val customerSession by viewModel.customerSession.collectAsState()

    LaunchedEffect(customerSession?.customer?.customerId) {
        viewModel.loadOrders(customerSession?.customer?.customerId)
    }

    Scaffold(
        containerColor = Color(0xFFF7F8FA),
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
                        text = "My Orders",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF118B3C))
                }
            }

            orders.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "No orders yet",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Text(
                            "Your future orders will appear here",
                            fontSize = 13.sp,
                            color = Color(0xFF75828A)
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(orders) { order ->
                        ThemedOrderCard(order = order) {
                            navController.navigate("${Routes.ORDER_DETAILS}/${order.orderId}")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ThemedOrderCard(
    order: CustomerOrderResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {

            // ✅ HEADER ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = "Order #${order.orderId}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF0E1F22)
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = order.status.name
                            .lowercase()
                            .replace("_", " ")
                            .replaceFirstChar { it.uppercase() },
                        fontSize = 13.sp,
                        color = statusColor(order.status),
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "View →",
                    color = Color(0xFF118B3C),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            // ✅ AMOUNT
            Text(
                text = "₱${"%,.2f".format(order.totalAmount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E1F22)
            )

            Spacer(Modifier.height(6.dp))

            // ✅ ADDRESS PREVIEW
            Text(
                text = order.deliveryAddress,
                fontSize = 13.sp,
                color = Color(0xFF75828A),
                maxLines = 2
            )
        }
    }
}

fun statusColor(status: OrderStatus): Color {
    return when (status) {
        OrderStatus.PENDING -> Color(0xFFFF9800)
        OrderStatus.PROCESSING -> Color(0xFF2196F3)
        OrderStatus.ON_DELIVERY -> Color(0xFF4CAF50)
        OrderStatus.CANCELLED -> Color(0xFFF44336)
        OrderStatus.DELIVERED -> Color(0xFF118B3C)
    }
}
