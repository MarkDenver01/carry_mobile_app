package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.nathaniel.carryapp.domain.enum.OrderStatus
import com.nathaniel.carryapp.domain.response.CustomerOrderResponse
import com.nathaniel.carryapp.domain.response.OrderResponse
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.membership.GoldMembershipCard
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.cart.CartViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopBottomBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopSearchBar
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.ui.state.LoginUiAction
import com.nathaniel.carryapp.presentation.ui.state.LoginUiEvent
import com.nathaniel.carryapp.presentation.utils.AnimatedLoaderOverlay
import com.nathaniel.carryapp.presentation.utils.LoadingOverlay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    val customerViewModel: CustomerViewModel = sharedViewModel()
    val orderViewModel: OrderViewModel = sharedViewModel()
    val cartViewModel: CartViewModel = sharedViewModel()
    val walletBalance by customerViewModel.walletBalance.collectAsState()
    val customer = customerViewModel.customerDetails.collectAsState().value
    val isLoading by customerViewModel.isLoading.collectAsState()
    val cartCount by cartViewModel.cartCount.collectAsState()
    var selectedIndex by remember { mutableStateOf(0) }
    val bannerCount by orderViewModel.bannerCount.collectAsState()
    val unreadCount by orderViewModel.unreadCount.collectAsState()
    // Orders state
    val customerSession by orderViewModel.customerSession.collectAsState()
    val myOrders by orderViewModel.orders.collectAsState()
    val ordersLoading by orderViewModel.isLoading.collectAsState()
    val membership by orderViewModel.membership.collectAsState()


    var searchQuery by remember { mutableStateOf("") }

    val sectionMap = mapOf(
        "wallet" to "Wallet",
        "my information" to "My Information",
        "suki" to "Suki Membership Program",
        "membership" to "Suki Membership Program",
        "orders" to "My Orders",
        "transactions" to "Transactions",
        "delivery" to "Delivery Address",
        "address" to "Delivery Address",
        "support" to "Support",
        "logout" to "Log out",
        "delete" to "Delete Account"
    )

    val isSearchActive = searchQuery.isNotBlank()

    fun shouldShowSection(tag: String): Boolean {
        if (!isSearchActive) return true
        return sectionMap.entries.any {
            it.value == tag && it.key.contains(searchQuery.lowercase())
        }
                || tag.lowercase().contains(searchQuery.lowercase())
    }

    LaunchedEffect(orderViewModel.loginUiAction) {
        orderViewModel.loginUiAction.collect { action ->
            when (action) {
                is LoginUiAction.Navigate -> {
                    navController.navigate(action.route) {
                        popUpTo(Routes.ORDERS) { inclusive = false }
                    }
                    orderViewModel.resetLoginAction()
                }

                is LoginUiAction.ShowToast -> "Account"
                null -> Unit
            }
        }
    }

    // 🔹 Load My Orders when we know customerId
    LaunchedEffect(customerSession?.customer?.customerId) {
        val customerId = customerSession?.customer?.customerId
        if (customerId != null) {
            orderViewModel.loadOrders(customerId)
            orderViewModel.loadMembership(customerId)
        }

    }

    Scaffold(
        containerColor = Color(0xFFF7F8FA),
        topBar = {
            ShopHeader(
                notifications = unreadCount,
                cartCount = cartCount,
                onCartClick = { navController.navigate(Routes.CART) },
                onNotificationClick = { navController.navigate(Routes.NOTIFICATIONS) }
            )
        },
        bottomBar = {
            ShopBottomBar(
                offersCount = bannerCount,
                selectedIndex = orderViewModel.selectedTab.collectAsState().value,
                onItemSelected = { orderViewModel.updateSelectedTab(it) },
                onHome = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnHomeClicked) },
                onCategories = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnCategoriesClicked) },
                onReorder = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnReorderClicked) },
                onAccount = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnAccountClicked) },
                onPromo = { orderViewModel.onLoginClickEvent(LoginUiEvent.OnPromoClicked) }
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
                        onSearch = { query ->
                            searchQuery = query
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            // ================================
            // 💳 WALLET CARD
            // ================================
            if (shouldShowSection("Wallet")) {
                item {
                    AccountCard {
                        Column(Modifier.padding(18.dp)) {

                            // Wallet Header
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_wallet),
                                    contentDescription = "",
                                    tint = Color(
                                        0xFF118B3C
                                    ),
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
                                    shape = RoundedCornerShape(15.dp)
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
            }

            // ================================
            // 👤 MY INFORMATION
            // ================================
            if (shouldShowSection("My Information")) {
                item {
                    SectionCard(title = "My Information") {
                        InfoRow("Name", customer?.userName ?: "")
                        InfoRow("Mobile Number", customer?.mobileNumber ?: "")
                        InfoRow("Email Address", customer?.email ?: "")
                        InfoRow("Deliver To", customer?.address ?: "")
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }

            // ================================
            // ⭐ SUKI MEMBERSHIP PROGRAM
            // ================================
            if (shouldShowSection("Suki Membership Program")) {
                item {
                    if (membership == null) {
                        SectionCard(title = "Suki Membership Program") {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(R.drawable.ic_membership), // 🔥 Add your own icon
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {

                                    Text(
                                        "Become a Suki Member!",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0E1F22)
                                    )

                                    Text(
                                        "Enjoy exclusive discounts, points, and freebies every order.",
                                        fontSize = 13.sp,
                                        color = Color(0xFF6F7F85)
                                    )
                                }

                                // 👉 BUTTON
                                Button(
                                    onClick = {
                                        navController.navigate(Routes.SUKI_MEMBERSHIP)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF118B3C
                                        )
                                    ),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    Text(
                                        "Join",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    } else {
                        val isExpired = isMembershipExpired(membership!!.expiryDate)

                        SectionCard(title = "Suki Membership Program") {

                            GoldMembershipCard(
                                name = customer?.userName ?: "",
                                photo = customer?.photoUrl,
                                points = membership!!.pointsBalance,
                                expiry = membership!!.expiryDate
                            )

                            // ✅ EXPIRED UI WARNING + RENEW BUTTON
                            if (isExpired) {

                                Spacer(Modifier.height(14.dp))

                                Text(
                                    text = "Membership Expired",
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )

                                Spacer(Modifier.height(10.dp))

                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF118B3C)
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    onClick = {
                                        orderViewModel.availMembership(
                                            customerId = customerSession?.customer?.customerId,
                                            walletBalance = walletBalance,
                                            onSuccess = {
                                                orderViewModel.loadMembership(
                                                    customerSession?.customer?.customerId ?: return@availMembership
                                                )
                                            },
                                            onDeductWallet = {
                                                customerViewModel.payMembershipFee()   // ✅ ACTUAL WALLET DEDUCT
                                            }
                                        )
                                    }
                                ) {
                                    Text(
                                        "Renew Membership ₱500",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                }
            }


            // ================================
            // 📦 MY ORDERS (WITH BACKEND)
            // ================================
            if (shouldShowSection("My Orders")) {
                item {
                    SectionCard(
                        title = "My Orders",
                        rightText = "View all",
                        onRightClick = { navController.navigate(Routes.ORDER_LIST) }
                    ) {

                        val latestOrder: CustomerOrderResponse? = myOrders.firstOrNull()

                        when {
                            ordersLoading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color(0xFF118B3C),
                                        strokeWidth = 3.dp
                                    )
                                }
                            }

                            latestOrder == null -> {
                                // EMPTY STATE (same as before)
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_empty_basket),
                                        contentDescription = "",
                                        tint = Color(0xFF118B3C),
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

                            else -> {
                                // 🔥 PREVIEW OF LATEST ORDER (SukiGrocer style)
                                LatestOrderPreview(
                                    order = latestOrder,
                                    onViewClick = {
                                        navController.navigate("${Routes.ORDER_DETAILS}/${latestOrder.orderId}")
                                    },
                                    onReceiptClick = {
                                        navController.navigate("${Routes.ORDER_RECEIPT}/${latestOrder.orderId}")
                                    }
                                )

                                Spacer(Modifier.height(12.dp))

                                Button(
                                    onClick = { navController.navigate(Routes.ORDER_LIST) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF118B3C)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "View All Orders",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }

            // ================================
            // 🧾 TRANSACTIONS (EMPTY)
            // ================================
            // ================================
            if (shouldShowSection("Transactions")) {
                item {

                    val paidOrders = myOrders.filter { it.status == OrderStatus.DELIVERED }

                    SectionCard(title = "Transactions") {

                        when {
                            ordersLoading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color(0xFF118B3C),
                                        strokeWidth = 3.dp
                                    )
                                }
                            }

                            paidOrders.isEmpty() -> {
                                // ✅ EMPTY STATE (UNCHANGED DESIGN)
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_reciept),
                                        contentDescription = "",
                                        tint = Color(0xFF118B3C),
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

                            else -> {
                                // ✅ SHOW PAID RECEIPTS
                                paidOrders.take(3).forEach { order ->

                                    TransactionRow(
                                        order = order,
                                        onReceiptClick = {
                                            navController.navigate(
                                                "${Routes.ORDER_RECEIPT}/${order.orderId}"
                                            )
                                        }
                                    )

                                    Divider(Modifier.padding(vertical = 10.dp))
                                }

                                // ✅ VIEW MORE BUTTON
                                Text(
                                    text = "View More Transactions →",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate(Routes.ORDER_LIST)
                                        },
                                    textAlign = TextAlign.End,
                                    color = Color(0xFF118B3C),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                }
            }

            // ================================
            // 📍 DELIVERY ADDRESS — LIVE MAP + REVERSE ADDRESS
            if (shouldShowSection("Delivery Address")) {
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
                                        title = "Your Delivery Address"
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
            }


            // ================================
            // 💬 SUPPORT
            // ================================
            if (shouldShowSection("Support")) {
                item {
                    AccountCard {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                painter = painterResource(id = R.drawable.ic_support),
                                contentDescription = "",
                                tint = Color(
                                    0xFF118B3C
                                ),
                                modifier = Modifier.size(70.dp)
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
            }

            // ================================
            // 🚪 LOG OUT + DELETE ACCOUNT
            // ================================
            item {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF118B3C)),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Log out", color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Spacer(Modifier.height(12.dp))
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
// 📌 SECTION CARD with Title + (optional) rightText click
// ======================================================================
@Composable
fun SectionCard(
    title: String? = null,
    rightText: String? = null,
    onRightClick: (() -> Unit)? = null,
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
                            fontWeight = FontWeight.Medium,
                            modifier = if (onRightClick != null) {
                                Modifier.clickable { onRightClick() }
                            } else {
                                Modifier
                            }
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

// ======================================================================
// 🧾 LATEST ORDER PREVIEW (SukiGrocer-style)
// ======================================================================
@Composable
fun LatestOrderPreview(
    order: CustomerOrderResponse,
    onViewClick: () -> Unit,
    onReceiptClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Order #${order.orderId}", fontWeight = FontWeight.SemiBold)

                Text(
                    order.status.name.replace("_", " "),
                    fontSize = 13.sp,
                    color = if (order.status.name == "CANCELLED") Color.Red else Color.Gray
                )
            }

            Text(
                "View",
                color = Color(0xFF118B3C),
                modifier = Modifier.clickable { onViewClick() }
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            "₱${"%,.2f".format(order.totalAmount)}",
            fontWeight = FontWeight.Bold
        )

        // ✅ RECEIPT BUTTON ONLY IF COMPLETED
        if (order.status.name == "DELIVERED") {
            Spacer(Modifier.height(8.dp))

            Text(
                text = "View Receipt",
                color = Color(0xFF118B3C),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onReceiptClick() }
            )
        }
    }
}

@Composable
fun TransactionRow(
    order: CustomerOrderResponse,
    onReceiptClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = "Order #${order.orderId}",
                fontWeight = FontWeight.Medium
            )

            Text(
                text = order.createdAt,
                fontSize = 12.sp,
                color = Color(0xFF6F7F85)
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "₱${"%,.2f".format(order.totalAmount)}",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "View Receipt",
                fontSize = 13.sp,
                color = Color(0xFF118B3C),
                modifier = Modifier.clickable { onReceiptClick() }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun isMembershipExpired(expiry: String?): Boolean {
    if (expiry.isNullOrBlank()) return true

    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val expiryDate = LocalDate.parse(expiry, formatter)
        expiryDate.isBefore(LocalDate.now())
    } catch (e: Exception) {
        true
    }
}