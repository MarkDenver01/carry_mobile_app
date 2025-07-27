package com.nathaniel.carryapp.presentation.ui.compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.nathaniel.carryapp.navigation.Routes

sealed class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
) {
    object Orders : BottomNavItem("Orders", Icons.Default.ShoppingCart, Routes.ORDERS)
    object Shopping : BottomNavItem("Shopping", Icons.Default.List, Routes.SHOPPING)
    object Home : BottomNavItem("Home", Icons.Default.Home, Routes.DASHBOARD)
    object Alerts : BottomNavItem("Alerts", Icons.Default.Notifications, Routes.ALERTS)
    object Browse : BottomNavItem("Browse", Icons.Default.Search, Routes.BROWSE)

    companion object {
        val item = listOf(Orders, Shopping, Home, Alerts, Browse)
    }
}