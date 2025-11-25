package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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

    // ⭐ CART Bounce
    val cartScale by animateFloatAsState(
        targetValue = if (cartCount > 0) 1f else 0.85f,
        animationSpec = tween(250, easing = FastOutSlowInEasing),
        label = "cartScale"
    )

    // ⭐ NOTIFICATION Bounce
    val notifScale by animateFloatAsState(
        targetValue = if (notifications > 0) 1f else 0.85f,
        animationSpec = tween(250, easing = FastOutSlowInEasing),
        label = "notifScale"
    )

    // ⭐ SHAKE — CART
    val previousCart = remember { mutableStateOf(cartCount) }
    val shouldShakeCart = cartCount > previousCart.value

    val cartShake by animateFloatAsState(
        targetValue = if (shouldShakeCart) 8f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "cartShake"
    )

    LaunchedEffect(cartCount) {
        if (cartCount > previousCart.value) previousCart.value = cartCount
    }

    // ⭐ SHAKE — NOTIFICATION
    val previousNotif = remember { mutableStateOf(notifications) }
    val shouldShakeNotif = notifications > previousNotif.value

    val notifShake by animateFloatAsState(
        targetValue = if (shouldShakeNotif) 8f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "notifShake"
    )

    LaunchedEffect(notifications) {
        if (notifications > previousNotif.value) previousNotif.value = notifications
    }

    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF2F7D32),
            actionIconContentColor = Color.White
        ),
        actions = {

            // 🔔 NOTIFICATION WITH SHAKE + SCALE + DOT
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        translationX = notifShake,
                        scaleX = notifScale,
                        scaleY = notifScale
                    )
                    .clickable { onNotificationClick() }
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_notification),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )

                if (notifications > 0) NotificationDot(notifications)
            }

            Spacer(Modifier.width(10.dp))

            // 🛒 CART WITH SHAKE + SCALE + BADGE
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        translationX = if (shouldShakeCart) cartShake else 0f,
                        scaleX = cartScale,
                        scaleY = cartScale
                    )
                    .alpha(if (isCartEmpty) 0.3f else 1f)
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

                if (cartCount > 0) CartBadge(cartCount)
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
