package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ShopBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onHome: () -> Unit,
    onCategories: () -> Unit,
    onReorder: () -> Unit,
    onAccount: () -> Unit,
    onPromo: () -> Unit = {},
    offersCount: Int = 0
) {
    val darkGreen = Color(0xFF0F8B3B)
    val lightGray = Color(0xFF6B7D85)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(90.dp) // extra height to allow the FAB to float
    ) {

        // 🔹 Bottom Navigation Bar (drawn FIRST, so it's under the FAB)
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(80.dp)
        ) {
            NavigationBarItem(
                selected = selectedIndex == 0,
                onClick = { onItemSelected(0); onHome() },
                icon = { Icon(Icons.Outlined.Home, "Home") },
                label = { Text("Home") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = lightGray,
                    selectedTextColor = darkGreen,
                    unselectedTextColor = lightGray,
                    indicatorColor = darkGreen
                )
            )

            NavigationBarItem(
                selected = selectedIndex == 1,
                onClick = { onItemSelected(1); onCategories() },
                icon = { Icon(Icons.Outlined.ShoppingCart, "Categories") },
                label = { Text("Categories") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = lightGray,
                    selectedTextColor = darkGreen,
                    unselectedTextColor = lightGray,
                    indicatorColor = darkGreen
                )
            )

            NavigationBarItem(
                selected = selectedIndex == 2,
                onClick = { onItemSelected(2); onReorder() },
                icon = { Icon(Icons.Outlined.List, "Reorder") },
                label = { Text("Reorder") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = lightGray,
                    selectedTextColor = darkGreen,
                    unselectedTextColor = lightGray,
                    indicatorColor = darkGreen
                )
            )

            NavigationBarItem(
                selected = selectedIndex == 3,
                onClick = { onItemSelected(3); onAccount() },
                icon = { Icon(Icons.Outlined.AccountCircle, "Account") },
                label = { Text("Account") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = lightGray,
                    selectedTextColor = darkGreen,
                    unselectedTextColor = lightGray,
                    indicatorColor = darkGreen
                )
            )
        }

        // 🔹 Center Floating PROMO Button (drawn LAST, so it’s on top)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-40).dp) // how much it floats above the bar
                .size(70.dp)
                .clip(CircleShape)
                .clickable { onPromo() },
            contentAlignment = Alignment.Center
        ) {
            PromoStarButton(
                count = offersCount,
                onClick = onPromo
            )
        }
    }
}

@Composable
fun PromoStarButton(
    count: Int,
    onClick: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0FA450),
            Color(0xFF0D7F3A)
        )
    )

    Box(
        modifier = Modifier
            .size(90.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val spikes = 24
            val outerRadius = size.minDimension / 2
            val innerRadius = outerRadius * 0.82f

            val center = Offset(size.width / 2, size.height / 2)
            val path = Path()

            for (i in 0 until spikes * 2) {
                val r = if (i % 2 == 0) outerRadius else innerRadius
                val angle = Math.toRadians((i * 360f / (spikes * 2)).toDouble()).toFloat()

                val x = center.x + r * cos(angle)
                val y = center.y + r * sin(angle)

                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            drawPath(path = path, brush = gradient)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = count.toString(),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "OFFERS",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

