package com.nathaniel.carryapp.presentation.ui.compose.orders.location

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackHeader(onBack: () -> Unit) {

    Surface(
        color = Color(0xFF0F8B3B),       // Same green header color
        shadowElevation = 6.dp           // Same elevation as ShopHeader
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()     // Prevent overlap with system bar
                .height(64.dp)           // Same height as ShopHeader
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            IconButton(
                onClick = onBack,
                modifier = Modifier.size(48.dp) // larger touch area
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,          // White icon for contrast
                )
            }
        }
    }
}
