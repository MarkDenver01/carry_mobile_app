package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import com.nathaniel.carryapp.presentation.utils.otp.OtpTextField
import com.nathaniel.carryapp.presentation.theme.LocalAppColors
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalAppTypography
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.sharedViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    navController: NavController,
    mobileNumber: String? = ""
) {
    val viewModel: SignInViewModel = sharedViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var otpCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var timer by remember { mutableStateOf(60) }

    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val typography = LocalAppTypography.current
    val sizes = LocalResponsiveSizes.current

    val backgroundColor = Color(0xFFF0FAF3)

    // Timer
    LaunchedEffect(Unit) {
        while (timer > 0) {
            delay(1000)
            timer--
        }
    }

    // Event Collector
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AuthUiEvent.NavigateToTerms -> {
                    navController.navigate("${Routes.AGREEMENT_TERMS_PRIVACY}/${event.mobileOrEmail}") {
                        popUpTo(Routes.OTP) { inclusive = true }
                    }
                }

                is AuthUiEvent.NavigateToDriver -> {
                    navController.navigate("${Routes.DRIVER_MAIN_SCREEN}/${event.mobileOrEmail}") {
                        popUpTo(Routes.OTP) { inclusive = true }
                    }
                }

                is AuthUiEvent.NavigateToHome -> {
                    navController.navigate(Routes.ORDERS) {
                        popUpTo(Routes.SIGN_IN) { inclusive = true }
                    }
                }

                is AuthUiEvent.ShowError -> {
                    errorMessage = event.message
                    otpCode = ""
                }

                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "OTP Verification",
                showMenuButton = false,
                showBackButton = true
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(backgroundColor)
                    .imePadding()
                    .padding(
                        horizontal = sizes.paddingHorizontal,
                        vertical = sizes.paddingVertical
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(spacing.md))

                // ============================
                //         TITLE
                // ============================
                Text(
                    text = "Enter the 5-digit code sent to",
                    fontSize = typography.body.fontSize,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(spacing.xs))

                Text(
                    text = formatMobileNumber(mobileNumber ?: ""),
                    fontSize = sizes.labelFontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111111)
                )

                Spacer(modifier = Modifier.height(spacing.xl))

                // ============================
                //        OTP INPUT
                // ============================
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
                        fontSize = typography.caption.fontSize,
                        modifier = Modifier.padding(top = spacing.sm)
                    )
                }

                Spacer(modifier = Modifier.height(spacing.xl))

                // ============================
                //       SUBMIT BUTTON
                // ============================
                DynamicButton(
                    onClick = {
                        viewModel.verifyOtp(mobileNumber ?: "", otpCode)
                    },
                    enabled = otpCode.length == 5 && !uiState.isLoading,
                    height = sizes.buttonHeight,
                    backgroundColor = colors.primary,
                    pressedBackgroundColor = colors.primaryDark,
                    content = if (uiState.isLoading) "Verifying..." else "Submit",
                    fontSize = sizes.buttonFontSize
                )

                Spacer(modifier = Modifier.height(spacing.lg))

                // ============================
                //      TIMER + RESEND
                // ============================
                if (timer > 0) {
                    Text(
                        text = "Request a new code in 00:${String.format("%02d", timer)}",
                        color = Color(0xFF777777),
                        fontSize = typography.caption.fontSize
                    )
                } else {
                    Row {
                        Text(
                            text = "Didn’t get the code? ",
                            color = Color(0xFF555555),
                            fontSize = typography.caption.fontSize
                        )
                        Text(
                            text = "Resend",
                            color = colors.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = typography.caption.fontSize,
                            modifier = Modifier.clickable {
                                timer = 60
                                viewModel.sendOtp(mobileNumber ?: "")
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            // ============================
            //     LOADING OVERLAY
            // ============================
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
        }
    }
}

private fun formatMobileNumber(number: String): String {
    return number.chunked(4).joinToString(" ")
}
