package com.nathaniel.carryapp.presentation.ui.compose.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.theme.LocalAppColors
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalAppTypography
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.utils.getAppVersionName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val versionName = remember { getAppVersionName(context) }

    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val typography = LocalAppTypography.current
    val sizes = LocalResponsiveSizes.current.copy(
        titleFontSize = 22.sp // 🔥 recommended max for phones
    )

    var mobileNumberOrEmailAddress by remember { mutableStateOf("") }

    // Lottie
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.banner))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AuthUiEvent.NavigateToOtp -> {
                    viewModel.saveMobileOrEmail(event.mobile)
                    navController.navigate("${Routes.OTP}/${event.mobile}") {
                        popUpTo(Routes.SIGN_IN) { inclusive = true }
                    }
                }

                is AuthUiEvent.NavigateToHome -> {
                    navController.navigate(Routes.ORDERS) {
                        popUpTo(Routes.SIGN_IN) { inclusive = true }
                    }
                }

                is AuthUiEvent.ShowError -> {
                    // show snackbar
                }

                else -> {}
            }
        }
    }

    val backgroundColor = Color(0xFFF0FAF3)

    Scaffold(
        containerColor = backgroundColor
    ) { innerPadding ->

        Box(   // <-- IMPORTANT: This allows overlay stacking
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // ================================
            // YOUR FULL SCREEN CONTENT
            // ================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(backgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- BANNER ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sizes.logoSize)
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0x66000000),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                // --- MAIN CONTENT BOX ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-spacing.xl))
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(backgroundColor)
                        .padding(
                            horizontal = sizes.paddingHorizontal,
                            vertical = sizes.paddingVertical
                        )
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Welcome to Wrap and Carry",
                            fontSize = sizes.titleFontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            softWrap = true,
                            maxLines = Int.MAX_VALUE
                        )

                        Text(
                            text = "Sign in with your mobile number",
                            fontSize = typography.body.fontSize,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(bottom = spacing.lg)
                        )

                        OutlinedTextField(
                            value = mobileNumberOrEmailAddress,
                            onValueChange = { mobileNumberOrEmailAddress = it },
                            placeholder = {
                                Text(
                                    text = "Mobile number or Email address",
                                    color = Color(0xFF666666),
                                    fontSize = typography.body.fontSize
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.primary,
                                unfocusedBorderColor = Color(0xFF777777),
                                cursorColor = colors.primary,
                                focusedTextColor = Color(0xFF111111),
                                unfocusedTextColor = Color(0xFF222222),
                                focusedPlaceholderColor = Color(0xFF666666),
                                unfocusedPlaceholderColor = Color(0xFF777777)
                            )
                        )

                        Spacer(modifier = Modifier.height(spacing.lg))

                        DynamicButton(
                            onClick = { viewModel.sendOtp(mobileNumberOrEmailAddress) },
                            height = sizes.buttonHeight,
                            backgroundColor = colors.primary,
                            pressedBackgroundColor = colors.primaryDark,
                            content = "Login or Register",
                            fontSize = sizes.buttonFontSize
                        )

                        Spacer(modifier = Modifier.height(spacing.md))

                        Text(
                            text = "Forgot Password?",
                            color = Color(0xFF009807),
                            fontSize = 14.sp,
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.FORGOT_PASSWORD)
                            }
                        )

                        Spacer(modifier = Modifier.height(spacing.xl))

                        Text(
                            text = "Version $versionName",
                            color = Color(0xFF444444),
                            fontSize = typography.caption.fontSize
                        )
                    }
                }
            }

            // ===========================================
            // 🔥 LOADING OVERLAY (ON TOP OF EVERYTHING)
            // ===========================================
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colors.primary
                    )
                }
            }
        }
    }
}
