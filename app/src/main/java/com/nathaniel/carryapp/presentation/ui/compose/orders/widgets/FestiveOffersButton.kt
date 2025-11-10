package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.*
import com.nathaniel.carryapp.R

@Composable
fun FestiveOfferIconButton(
    offersCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 🌿 Smooth breathing pulse between subtle green shades
    val infiniteTransition = rememberInfiniteTransition(label = "offerPulse")

    val pulseColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF11C870),
        targetValue = Color(0xFF0F8B3B),
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseColor"
    )

    // ✨ Sparkle overlay (from raw/sparkle.json)
    val sparkle by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sparkle))
    val sparkleProgress by animateLottieCompositionAsState(
        composition = sparkle,
        iterations = LottieConstants.IterateForever
    )

    // 🟢 Festive circle button
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(pulseColor)
            .border(
                width = 1.dp,
                color = Color(0xFFE6ECEF), // ✅ light gray border for modern flat look
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // ✨ Sparkle animation overlay (subtle brightness)
        LottieAnimation(
            composition = sparkle,
            progress = { sparkleProgress },
            modifier = Modifier
                .matchParentSize()
                .zIndex(1f)
        )

        // 🎁 Centered text with perfect spacing
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .zIndex(2f)
                .padding(top = 2.dp) // ✅ small offset to perfectly center text visually
        ) {
            Text(
                text = offersCount.toString(),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 34.sp
            )
            Spacer(modifier = Modifier.height(0.dp)) // ✅ tightened spacing between lines
            Text(
                text = "Offer",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp
            )
        }
    }
}
