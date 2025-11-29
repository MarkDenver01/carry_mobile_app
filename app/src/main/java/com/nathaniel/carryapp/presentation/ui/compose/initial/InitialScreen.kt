package com.nathaniel.carryapp.presentation.ui.compose.initial

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.theme.BgApp
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.service.DriverLocationScheduler
import com.nathaniel.carryapp.presentation.ui.service.DriverLocationService
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import kotlinx.coroutines.delay

@Composable
fun InitialScreen(
    navController: NavController,
    viewModel: InitialViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    // Observe navigation flow
    LaunchedEffect(Unit) {
        viewModel.navigateToDashboard.collect {
            navController.navigate(Routes.ORDERS) {
                popUpTo(Routes.INITIAL) { inclusive = true }
            }
        }
    }

    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // ✅ White background for clean look
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing.lg, vertical = spacing.xl)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔰 App Logo
            Image(
                painter = painterResource(id = R.drawable.logo_final),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(sizes.logoSize)
                    .padding(bottom = spacing.lg)
            )

            // 🟢 App Title
            Text(
                text = "Wrap & Carry",
                fontSize = sizes.titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F8B3B), // same green as header
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing.md))

            // 📝 Subtitle or Tagline
            Text(
                text = "Your trusted everyday grocery partner.",
                fontSize = 14.sp,
                color = Color(0xFF6B7D85),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = spacing.lg)
            )

            Spacer(modifier = Modifier.height(spacing.xl * 2))

            // ✅ Get Started Button
            var pressed by remember { mutableStateOf(false) }
            DynamicButton(
                onClick = {
                    pressed = true
                    //viewModel.onGetStartedClicked()
//                    val intent = Intent(context, DriverLocationService::class.java).apply {
//                        action = "START_DRIVER_SERVICE"
//                    }
//                    ContextCompat.startForegroundService(context, intent)
                    DriverLocationScheduler.start(context)
                },
                height = sizes.buttonHeight,
                fontSize = sizes.buttonFontSize,
                backgroundColor = BgApp, // uses your green theme
                content = "Get Started!"
            )

            LaunchedEffect(pressed) {
                if (pressed) {
                    delay(120)
                    pressed = false
                }
            }

            Spacer(modifier = Modifier.height(spacing.xl * 2))
        }
    }
}
