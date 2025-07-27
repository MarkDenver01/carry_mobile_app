package com.nathaniel.carryapp.presentation.ui.compose.initial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.theme.BgApp
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.utils.DynamicButton
import kotlinx.coroutines.delay

@Composable
fun InitialScreen(
    navController: NavController,
    viewModel: InitialViewModel = hiltViewModel()
) {
    // Navigation observer
    LaunchedEffect(Unit) {
        viewModel.navigateToDashboard.collect {
            navController.navigate(Routes.SIGN_IN) {
                popUpTo(Routes.INITIAL) { inclusive = true }
            }
        }
    }

    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = spacing.lg,
                    vertical = spacing.xl
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            // Logo & Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Wrap & Carry",
                    fontSize = sizes.titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = spacing.md)
                )

                Image(
                    painter = painterResource(id = R.drawable.logo_final),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(sizes.logoSize)
                        .padding(bottom = spacing.lg)
                )
            }

            Spacer(modifier = Modifier.height(spacing.xl))

            // Animated button
            var pressed by remember { mutableStateOf(false) }

            DynamicButton(
                onClick = {
                    pressed = true
                    viewModel.onGetStartedClicked()
                },
                height = sizes.buttonHeight,
                fontSize = sizes.buttonFontSize,
                backgroundColor = BgApp,
                content = "Get Started!"
            )

            Spacer(modifier = Modifier.weight(1.2f))

            LaunchedEffect(pressed) {
                if (pressed) {
                    delay(120)
                    pressed = false
                }
            }
        }
    }
}
