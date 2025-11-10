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

@Composable
fun ShopBottomBar(
    onHome: () -> Unit,
    onCategories: () -> Unit,
    onReorder: () -> Unit,
    onAccount: () -> Unit,
    offersCount: Int = 0
) {
    var selectedIndex by remember { mutableStateOf(0) } // 0=Home,1=Categories,2=Reorder,3=Account
    val darkGreen = Color(0xFF0F8B3B)
    val lightGray = Color(0xFF6B7D85)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        // ✅ Bottom Navigation Bar with proper system padding
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // ✅ fixes overlap with Android nav bar
                .height(80.dp)            // ✅ gives breathing space for icons and labels
        ) {
            // HOME
            NavigationBarItem(
                selected = selectedIndex == 0,
                onClick = { selectedIndex = 0; onHome() },
                icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                label = { Text("Home") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = lightGray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = lightGray,
                    indicatorColor = darkGreen
                )
            )

            // CATEGORIES
            NavigationBarItem(
                selected = selectedIndex == 1,
                onClick = { selectedIndex = 1; onCategories() },
                icon = { Icon(Icons.Outlined.ShoppingCart, contentDescription = "Categories") },
                label = { Text("Categories") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = lightGray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = lightGray,
                    indicatorColor = darkGreen
                )
            )

            // Spacer for floating "Offers" circle
            Spacer(modifier = Modifier.width(56.dp))

            // REORDER
            NavigationBarItem(
                selected = selectedIndex == 2,
                onClick = { selectedIndex = 2; onReorder() },
                icon = { Icon(Icons.Outlined.List, contentDescription = "Reorder") },
                label = { Text("Reorder") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = lightGray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = lightGray,
                    indicatorColor = darkGreen
                )
            )

            // ACCOUNT
            NavigationBarItem(
                selected = selectedIndex == 3,
                onClick = { selectedIndex = 3; onAccount() },
                icon = { Icon(Icons.Outlined.AccountCircle, contentDescription = "Account") },
                label = { Text("Account") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = lightGray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = lightGray,
                    indicatorColor = darkGreen
                )
            )
        }

        // ✅ Floating “OFFERS” Pill — sits above without hiding anything
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp) // lifted just above icons cleanly
                .size(68.dp)
                .clip(CircleShape)
                .background(Color(0xFF11C870)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = offersCount.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "OFFERS",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
