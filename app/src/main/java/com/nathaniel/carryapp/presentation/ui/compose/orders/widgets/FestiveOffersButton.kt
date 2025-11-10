package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.*
import com.nathaniel.carryapp.R

@Composable
fun FestiveOfferIconButton(
    offersCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Color(0xFF11C870))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // ✨ Sparkle effect (from raw/sparkle.json)
        val sparkle by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sparkle))
        val sparkleProgress by animateLottieCompositionAsState(
            composition = sparkle,
            iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            composition = sparkle,
            progress = { sparkleProgress },
            modifier = Modifier
                .matchParentSize()
                .zIndex(1f)
        )

        // 🎁 Offer text (clean and centered)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.zIndex(2f)
        ) {
            Text(
                text = offersCount.toString(),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Offer",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
