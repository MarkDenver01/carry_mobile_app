package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopBottomBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopSearchBar
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.utils.AnimatedLoaderOverlay
import com.nathaniel.carryapp.presentation.utils.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    val customerViewModel: CustomerViewModel = sharedViewModel()
    val orderViewModel: OrderViewModel = sharedViewModel()
    val walletBalance by customerViewModel.walletBalance.collectAsState()
    val customer = customerViewModel.customerDetails.collectAsState().value
    val isLoading by customerViewModel.isLoading.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = { ShopHeader(notifications = 12, cartCount = 7) },
        bottomBar = {
            ShopBottomBar(
                onHome = { navController.navigate(Routes.ORDERS) },
                onCategories = { navController.navigate(Routes.CATEGORIES) },
                onReorder = { navController.navigate(Routes.REORDER) },
                onAccount = {}
            )
        }
    ) { inner ->

        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {

            // ================================
            // 🔍 SEARCH BAR
            // ================================
            item {
                Column {
                    ShopSearchBar(
                        hint = "I'm looking for…",
                        onSearch = {}
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            // ================================
            // 💳 WALLET CARD
            // ================================
            item {
                AccountCard {
                    Column(Modifier.padding(18.dp)) {

                        // Wallet Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_wallet),
                                contentDescription = "",
                                tint = Color(0xFF0E1F22),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Wallet",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0E1F22)
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        // Wallet Balance + Cash In Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                Text(
                                    "WALLET BALANCE",
                                    fontSize = 13.sp,
                                    color = Color(0xFF6F7F85)
                                )

                                Text(
                                    "₱${"%,.2f".format(walletBalance)}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0E1F22)
                                )
                            }

                            Button(
                                onClick = {
                                    navController.navigate(Routes.CASH_IN) {
                                        popUpTo(Routes.ACCOUNT) { inclusive = true }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF118B3C
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    "Cash in",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
            }

            // ================================
            // 👤 MY INFORMATION
            // ================================
            item {
                SectionCard(title = "My Information") {
                    InfoRow("Name", customer?.userName ?: "")
                    InfoRow("Mobile Number", customer?.mobileNumber ?: "")
                    InfoRow("Email Address", customer?.email ?: "")
                    InfoRow("Deliver To", customer?.address ?: "")
                }
                Spacer(Modifier.height(20.dp))
            }

            // ================================
            // 📦 MY ORDERS (EMPTY)
            // ================================
            item {
                SectionCard(
                    title = "My Orders",
                    rightText = "View all"
                ) {

                    Spacer(Modifier.height(4.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Image(
                            painter = painterResource(R.drawable.ic_empty_basket),
                            contentDescription = "",
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "You don't have any orders yet.",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0E1F22),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            "Your orders will appear here.",
                            fontSize = 13.sp,
                            color = Color(0xFF75828A),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // ================================
            // 🧾 TRANSACTIONS (EMPTY)
            // ================================
            item {
                SectionCard {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_reciept),
                            contentDescription = "",
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(Modifier.height(12.dp))

                        Text(
                            "No transactions yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            "Your payment transactions will appear here.",
                            fontSize = 13.sp,
                            color = Color(0xFF75828A),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // ================================
            // 📍 DELIVERY ADDRESS — LIVE MAP + REVERSE ADDRESS
            item {

                val reverseAddress by orderViewModel.reverseAddress.collectAsState()
                val pinPosition by orderViewModel.selectedLatLng.collectAsState()

                // Load map UI
                SectionCard(title = "Delivery Address") {

                    Text(
                        "Problem with your delivery address? Request Change",
                        color = Color(0xFF118B3C),
                        fontSize = 13.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    // GOOGLE MAP DISPLAY
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(
                            pinPosition ?: LatLng(14.0645, 121.1460),
                            16f
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = false,
                                myLocationButtonEnabled = false
                            )
                        ) {
                            pinPosition?.let {
                                Marker(
                                    state = MarkerState(position = it),
                                    title = "Your Home Address"
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // SHOW EXACT REVERSE GEOCODED ADDRESS
                    reverseAddress.fullAddressLine?.let {
                        Text(
                            it,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "View Details →",
                        color = Color(0xFF118B3C),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(20.dp))
            }


            // ================================
            // 💬 SUPPORT
            // ================================
            item {
                AccountCard {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(R.drawable.ic_support),
                            contentDescription = "",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(40.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(14.dp))

                        Column {
                            Text(
                                "Having problems with your balances and payments?",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Chat with Support →",
                                fontSize = 14.sp,
                                color = Color(0xFF118B3C)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(28.dp))
            }

            // ================================
            // 🚪 LOG OUT + DELETE ACCOUNT
            // ================================
            item {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Log out", color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "Delete my account",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF6F7F85),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        AnimatedLoaderOverlay(isLoading)
    }
}

// ======================================================================
// 🎴 WRAPPER CARD (Soft Shadow)
// ======================================================================
@Composable
fun AccountCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        content()
    }
}

// ======================================================================
// 📌 SECTION CARD with Title
// ======================================================================
@Composable
fun SectionCard(
    title: String? = null,
    rightText: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    AccountCard {
        Column(Modifier.padding(18.dp)) {

            if (title != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (rightText != null) {
                        Text(
                            rightText,
                            fontSize = 14.sp,
                            color = Color(0xFF118B3C),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            content()
        }
    }
}

// ======================================================================
// 🔹 LABEL + VALUE ROW
// ======================================================================
@Composable
fun InfoRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(label, fontSize = 13.sp, color = Color(0xFF6F7F85))
        Text(value, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}
