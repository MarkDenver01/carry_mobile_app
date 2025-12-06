package com.nathaniel.carryapp.presentation.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nathaniel.carryapp.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedLoaderOverlay(isLoading: Boolean) {
    // Internal state to control actual visibility
    var visible by remember { mutableStateOf(isLoading) }

    // Minimum display time = 1 to 2 seconds (randomized)
    val minDisplayTime = remember { (1000L..2000L).random() }

    // Handle delay BEFORE hiding loader
    LaunchedEffect(isLoading) {
        if (isLoading) {
            visible = true
        } else {
            delay(minDisplayTime)
            visible = false
        }
    }

    if (!visible) return

    // 🔄 Infinite rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "overlay_loader")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
        ),
        label = "overlay_angle"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_broken_image), // your animated icon
            contentDescription = "Loading",
            tint = Color.DarkGray,
            modifier = Modifier
                .size(120.dp)
                .rotate(angle)
        )
    }
}
