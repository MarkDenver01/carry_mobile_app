package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopHeader(
    notifications: Int = 0,
    cartCount: Int,
    onCartClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val isCartEmpty = cartCount == 0

    // 🔍 Slight scale change (para may konting bounce / presence)
    val cartScale by animateFloatAsState(
        targetValue = if (cartCount > 0) 1f else 0.9f,
        animationSpec = tween(
            durationMillis = 180,
            easing = FastOutSlowInEasing
        ),
        label = "cartScale"
    )

    // 💥 TRUE SHAKE ANIMATION (left-right wiggle)
    val cartShake = remember { Animatable(0f) }
    var previousCartCount by remember { mutableStateOf(cartCount) }

    LaunchedEffect(cartCount) {
        // Only shake when count **increases** (may bagong item)
        if (cartCount > previousCartCount) {
            cartShake.snapTo(0f)
            cartShake.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 420

                    // left-right wiggle pattern
                    -8f at 60
                    8f at 120
                    -6f at 180
                    6f at 240
                    -3f at 300
                    3f at 360
                    0f at 420
                }
            )
        }
        previousCartCount = cartCount
    }

    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF2F7D32), // 🟩 green header
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        actions = {

            // 🔔 NOTIFICATION ICON (steady, no shake)
            IconButton(onClick = onNotificationClick) {
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                    if (notifications > 0) {
                        NotificationDot(number = notifications)
                    }
                }
            }

            Spacer(Modifier.width(10.dp))

            // 🛒 CART ICON (with shake + disable logic)
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        translationX = cartShake.value, // 💥 shake offset
                        scaleX = cartScale,
                        scaleY = cartScale
                    )
                    .alpha(if (isCartEmpty) 0.3f else 1f) // dim when empty
                    .clickable(enabled = !isCartEmpty) {
                        if (!isCartEmpty) onCartClick()
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )

                if (cartCount > 0) {
                    CartBadge(count = cartCount)
                }
            }

            Spacer(Modifier.width(14.dp))
        }
    )
}

@Composable
fun CartBadge(count: Int) {
    Box(
        modifier = Modifier
            .offset(x = 14.dp, y = (-6).dp)
            .size(18.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE53935)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}

@Composable
fun NotificationDot(number: Int) {
    Box(
        modifier = Modifier
            .offset(x = 10.dp, y = (-6).dp)
            .size(18.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE53935)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}
