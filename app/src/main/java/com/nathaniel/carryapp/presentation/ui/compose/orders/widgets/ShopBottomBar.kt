package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nathaniel.carryapp.R

@Composable
fun ShopBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onHome: () -> Unit,
    onCategories: () -> Unit,
    onReorder: () -> Unit,
    onAccount: () -> Unit,
    offersCount: Int = 0
) {
    val darkGreen = Color(0xFF0F8B3B)
    val lightGray = Color(0xFF6B7D85)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
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
                selectedTextColor = Color.White,
                unselectedTextColor = lightGray,
                indicatorColor = darkGreen
            )
        )

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { onItemSelected(1); onCategories() },
            icon = { Icon(Icons.Outlined.ShoppingCart, "Categories") },
            label = { Text("Categories") }
        )

        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { onItemSelected(2); onReorder() },
            icon = { Icon(Icons.Outlined.List, "Reorder") },
            label = { Text("Reorder") }
        )

        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = { onItemSelected(3); onAccount() },
            icon = { Icon(Icons.Outlined.AccountCircle, "Account") },
            label = { Text("Account") }
        )
    }
}
