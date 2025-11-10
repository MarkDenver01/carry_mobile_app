package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopHeader(
    notifications: Int,
    cartCount: Int
) {
    // Prevent system bar overlap and add elevation
    Surface(
        color = Color(0xFF0F8B3B),
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // ✅ fix overlap with system status bar
                .height(64.dp)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            // ✅ Left side: Logo + "Wrap and Carry"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                // Logo slightly taller for visual balance
                Image(
                    painter = painterResource(id = R.drawable.logo_final),
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(38.dp)
                        .width(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Align text vertically with logo center
                Text(
                    text = "Wrap and Carry",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 2.dp) // fine-tune for perfect alignment
                )
            }

            // ✅ Right side: Cart and Notifications
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                // Cart Icon with Badge
                BadgedBox(
                    badge = {
                        if (cartCount > 0)
                            Badge(containerColor = Color(0xFFFF6B00)) {
                                Text(cartCount.toString())
                            }
                    }
                ) {
                    IconButton(onClick = { /* TODO: cart */ }) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.White
                        )
                    }
                }

                // Notifications Icon with Badge
                BadgedBox(
                    badge = {
                        if (notifications > 0)
                            Badge(containerColor = Color(0xFFFF6B00)) {
                                Text(notifications.toString())
                            }
                    }
                ) {
                    IconButton(onClick = { /* TODO: notifications */ }) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
