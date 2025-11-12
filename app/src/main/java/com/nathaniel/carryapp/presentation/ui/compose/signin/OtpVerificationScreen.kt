package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import com.nathaniel.carryapp.presentation.utils.otp.OtpTextField
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    navController: NavController,
    mobileNumber: String? = "",
    viewModel: SignInViewModel = hiltViewModel()
) {
    val verifyState by viewModel.verifyState.collectAsState()

    var otpCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var timer by remember { mutableStateOf(60) }

    val backgroundColor = Color(0xFFF0FAF3)
    val isButtonEnabled = otpCode.length == 6 && verifyState !is NetworkResult.Loading

    // Timer countdown
    LaunchedEffect(Unit) {
        while (timer > 0) {
            delay(1000L)
            timer--
        }
    }

    // React to verification results
    LaunchedEffect(verifyState) {
        when (verifyState) {
            is NetworkResult.Success -> {
                navController.navigate(Routes.ORDERS) {
                    popUpTo(Routes.SIGN_IN) { inclusive = true }
                }
            }

            is NetworkResult.Error -> {
                errorMessage = verifyState.message ?: "Incorrect OTP. Please try again."
                otpCode = ""
            }

            else -> Unit
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "OTP Verification",
                showMenuButton = false, // remove menu button for auth flow
                showBackButton = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Enter the 6-digit code sent to",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formatMobileNumber(mobileNumber ?: ""),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // === OTP Input Field ===
            OtpTextField(
                otpText = otpCode,
                onOtpTextChange = { value, complete ->
                    otpCode = value
                    if (complete) errorMessage = null
                }
            )

            AnimatedVisibility(visible = errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // === Verify Button ===
            DynamicButton(
                onClick = { viewModel.verifyOtp(mobileNumber ?: "", otpCode) },
                enabled = isButtonEnabled,
                height = 55.dp,
                backgroundColor = Color(0xFF4CAF50),
                pressedBackgroundColor = Color(0xFF388E3C),
                content = when (verifyState) {
                    is NetworkResult.Loading -> "Verifying..."
                    else -> "Submit"
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // === Timer / Resend Section ===
            if (timer > 0) {
                Text(
                    text = "Request a new code in 00:${String.format("%02d", timer)}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                Row {
                    Text(
                        text = "Didn’t get the code? ",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Resend",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            timer = 60
                            viewModel.sendOtp(mobileNumber ?: "")
                        }
                    )
                }
            }
        }

        // === Optional Full-Screen Loader Overlay ===
        if (verifyState is NetworkResult.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4CAF50))
            }
        }
    }
}

private fun formatMobileNumber(number: String): String {
    // Example: 09693342612 → "0969 334 2612"
    return number.chunked(4).joinToString(" ")
}
