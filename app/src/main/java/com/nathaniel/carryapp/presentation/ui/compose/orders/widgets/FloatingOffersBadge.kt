package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.nathaniel.carryapp.R

@Composable
fun FloatingSnowballButton(
    modifier: Modifier = Modifier,
    count: Int = 5,
    onClick: () -> Unit
) {
    // ❄ Gentle bounce animation
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val bounce by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // ❄ Snowflake drift animation
    val drift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Interaction source for ripple feedback
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(115.dp)
            .offset(y = (-32).dp + bounce.dp)
            .clip(CircleShape) // Ensures ripple & touch area follow the circle
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,          // ripple follows circle bounds
                    radius = 60.dp,          // approximate circle radius
                    color = Color(0x33000000) // soft gray ripple
                )
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {

        // ❄ Soft outer glow
        Box(
            modifier = Modifier
                .size(110.dp)
                .graphicsLayer {
                    shadowElevation = 30f
                    shape = CircleShape
                    clip = true
                }
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x66FFFFFF),
                            Color.Transparent
                        ),
                        radius = 180f
                    )
                )
        )

        // ❄ Main snowball body
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFE5F7FF),
                            Color(0xFFCDE8FF)
                        ),
                        radius = 140f
                    )
                )
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x55000000)
                ),
            contentAlignment = Alignment.Center
        ) {

            // ❄ Snowflakes inside the button
            Canvas(modifier = Modifier.fillMaxSize()) {
                val flakeColor = Color(0xAAFFFFFF)
                val flakeSmall = size.minDimension * 0.06f
                val flakeLarge = size.minDimension * 0.1f

                val offsetY = drift / 3f

                // Small flakes
                drawCircle(flakeColor, flakeSmall, Offset(size.width * .25f, size.height * (.25f + offsetY)))
                drawCircle(flakeColor, flakeSmall, Offset(size.width * .75f, size.height * (.35f + offsetY)))
                drawCircle(flakeColor, flakeSmall, Offset(size.width * .55f, size.height * (.65f + offsetY)))

                // Large flakes
                drawCircle(flakeColor, flakeLarge, Offset(size.width * .40f, size.height * (.45f - offsetY)))
                drawCircle(flakeColor, flakeLarge, Offset(size.width * .70f, size.height * (.75f - offsetY)))
            }

            // ❄ Gloss highlight
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .offset(y = (-10).dp)
                    .blur(18.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(Color.White.copy(alpha = 0.7f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )

            // ❄ Text label (count + label)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$count",
                    color = Color(0xFF0E1F22),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp
                )
                Text(
                    text = "OFFERS",
                    color = Color(0xFF0E1F22),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
