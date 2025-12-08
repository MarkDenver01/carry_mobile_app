package com.nathaniel.carryapp.presentation.ui.compose.membership

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nathaniel.carryapp.domain.enum.AlertType
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.account.CustomerViewModel
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.utils.SweetAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SukiMembershipScreen(navController: NavController) {

    val orderViewModel: OrderViewModel = sharedViewModel()
    val customerViewModel: CustomerViewModel = sharedViewModel()
    val customerSession by orderViewModel.customerSession.collectAsState()
    val customer = customerViewModel.customerDetails.collectAsState().value
    val wallet by customerViewModel.walletBalance.collectAsState()
    val membership by orderViewModel.membership.collectAsState()
    var showDialogSuccessPayment by remember { mutableStateOf(false) }
    val walletBalance by customerViewModel.walletBalance.collectAsState()

    LaunchedEffect(customerSession?.customer?.customerId) {
        customerSession?.customer?.customerId?.let {
            orderViewModel.loadMembership(it)
        }
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
                        text = "Suki Membership Program",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
        ) {

            // ✅ GOLD CARD
            if (membership != null) {

                GoldMembershipCard(
                    name = customer?.userName ?: "",
                    photo = customer?.photoUrl,
                    points = membership!!.pointsBalance,
                    expiry = membership!!.expiryDate
                )

            } else {

                Text("Join Suki Membership", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(16.dp))

                Text("Membership Fee: ₱500")

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        customerViewModel.payMembershipFee()
                        val customerId = customerSession?.customer?.customerId
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
                    },
                    enabled = wallet >= 500,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text("Pay ₱500 & Join", color = Color.Black)
                }

                if (wallet < 500) {
                    Spacer(Modifier.height(10.dp))
                    Text("Insufficient wallet balance", color = Color.Red)
                }
            }
        }
    }

    if (showDialogSuccessPayment) {
        SweetAlertDialog(
            type = AlertType.SUCCESS,
            title = "Payment Success",
            message = "Thank you for registering a SUKI member program. Enjoy the privileges that we OFFER.",
            show = showDialogSuccessPayment,
            confirmText = "Close",
            isSingleButton = true,
            onConfirm = {
                showDialogSuccessPayment = false

                // ✅ REFRESH WALLET FROM BACKEND
                customerViewModel.refreshWallet()

                // ✅ NAVIGATE
                navController.navigate(Routes.ACCOUNT) {
                    popUpTo(Routes.SUKI_MEMBERSHIP) { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun GoldMembershipCard(
    name: String,
    photo: String?,
    points: Int,
    expiry: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFD700)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(20.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                if (photo != null) {
                    AsyncImage(
                        model = photo,
                        contentDescription = "",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Suki Gold Member", fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            Text("Points: $points", fontWeight = FontWeight.Bold)
            Text("Expiry: $expiry")
        }
    }
}

