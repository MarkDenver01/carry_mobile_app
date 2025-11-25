package com.nathaniel.carryapp.presentation.ui.compose.orders.account

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.components.BackHeader
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import com.nathaniel.carryapp.presentation.utils.AnimatedLoaderOverlay
import com.nathaniel.carryapp.presentation.utils.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashInScreen(
    navController: NavController) {
    val viewModel: CustomerViewModel = sharedViewModel()
    var amount by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    val cashInUrl by viewModel.cashInUrl.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()
    val context = LocalContext.current

    val isLoading by viewModel.isLoading.collectAsState()

    // 🔥 Refresh balance when opening screen
    LaunchedEffect(Unit) {
        viewModel.refreshWallet()
    }

    // open GCash / Xendit URL when available
    LaunchedEffect(cashInUrl) {
        cashInUrl?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            viewModel.clearCashInUrl()
        }
    }

    Scaffold(
        topBar = {
            BackHeader(
                title = "Cash In",
                onBack = { navController.popBackStack() }
            )
        },
        containerColor = Color(0xFFF5F6F8),
        bottomBar = {
            CashInBottomBar(
                amount = amount.ifBlank { "0.00" },
                onCashIn = { viewModel.onCashInClicked(amount) }
            )
        }
    ) { inner ->

        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "How much will you cash in?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF7A7A7A)
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "₱",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2A2A2A)
                        )

                        Spacer(Modifier.width(6.dp))

                        TextField(
                            value = amount,
                            onValueChange = { newValue ->
                                val noCommas = newValue.replace(",", "")
                                if (noCommas.matches(Regex("^\\d{0,9}(\\.\\d{0,2})?$"))) {
                                    amount = formatAmountInput(newValue)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { focusState ->
                                    if (isFocused && !focusState.isFocused) {
                                        if (amount.isNotBlank()) {
                                            amount = enforceTwoDecimalPlaces(amount)
                                        }
                                    }
                                    isFocused = focusState.isFocused
                                },
                            placeholder = {
                                Text(
                                    "0.00",
                                    fontSize = 28.sp,
                                    color = Color(0xFFBDBDBD)
                                )
                            },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )
                    }

                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Available Balance",
                            color = Color(0xFF7A7A7A),
                            fontSize = 14.sp
                        )
                        Text(
                            "₱${"%,.2f".format(walletBalance)}",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        AnimatedLoaderOverlay(isLoading)
    }
}

@Composable
fun CashInBottomBar(
    amount: String,
    onCashIn: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {

        Button(
            onClick = onCashIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF118B3C)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                "Cash in ₱$amount",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}


fun enforceTwoDecimalPlaces(input: String): String {
    if (input.isBlank()) return ""

    val clean = input.replace(",", "")

    return try {
        val value = clean.toDouble()
        "%,.2f".format(value)  // always 2 decimals
    } catch (_: Exception) {
        input
    }
}

fun formatAmountInput(input: String): String {
    if (input.isEmpty()) return ""

    // Remove commas first
    val clean = input.replace(",", "")

    // Prevent invalid
    if (clean == ".") return ""

    return try {
        val parts = clean.split(".")
        val whole = parts[0].toLongOrNull() ?: return ""
        val decimal = if (parts.size > 1) parts[1].take(2) else ""

        val formattedWhole = "%,d".format(whole)

        if (decimal.isNotEmpty()) "$formattedWhole.$decimal"
        else formattedWhole

    } catch (_: Exception) {
        input
    }
}


@Composable
fun CashInBottomBar(amount: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF118B3C)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                "Cash in ₱$amount",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
