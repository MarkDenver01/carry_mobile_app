package com.nathaniel.carryapp.presentation.ui.compose.signin

import android.os.Build
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
import com.nathaniel.carryapp.domain.request.SignInRequest
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import com.nathaniel.carryapp.presentation.utils.getAppVersionName
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val versionName = remember { getAppVersionName(context) }

    var mobileNumber by remember { mutableStateOf("") }

    // Lottie composition setup
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.banner))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    // ✅ Unified background color
    val backgroundColor =
        Color(0xFFF0FAF3) // pick your color here (light green tint, not white/gray)

    val otpState by viewModel.otpState.collectAsState()

    LaunchedEffect(otpState) {
        when (otpState) {
            is NetworkResult.Success -> {
                navController.navigate("${Routes.OTP}/$mobileNumber") {
                    popUpTo(Routes.SIGN_IN) { inclusive = true }
                }
            }
            is NetworkResult.Error -> {
                Timber.e("Cannot send verification code")
            }
            is NetworkResult.Loading -> {
                Timber.w("still loading..")
            }
        }
    }

    Scaffold(
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(backgroundColor), // same color as scaffold
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // === Banner Section ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // Lottie Animation Background
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                // Optional overlay for contrast
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x66000000),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            // === Main Content Box (same color as background) ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-40).dp)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(backgroundColor) // ✅ same color
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Welcome to Wrap and Carry",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Sign in with your mobile number",
                        fontSize = 14.sp,
                        color = Color(0xFF388E3C),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        placeholder = { Text("09xxxxxxxxx") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFFBDBDBD),
                            cursorColor = Color(0xFF4CAF50)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DynamicButton(
                        onClick = {
                            viewModel.sendOtp(mobileNumber)
                        },
                        height = 55.dp,
                        backgroundColor = Color(0xFF4CAF50),
                        pressedBackgroundColor = Color(0xFF388E3C),
                        content = "Login or Register"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Forgot Password?",
                        color = Color(0xFF4CAF50),
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.FORGOT_PASSWORD)
                        }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "Version $versionName",
                        color = Color(0xFF388E3C),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
