package com.nathaniel.carryapp.presentation.ui.compose.orders.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import coil.compose.AsyncImage

@Composable
fun ProductCard(
    imageUrl: String,
    name: String,
    weight: String,
    sold: String,
    price: String,
    onFavorite: () -> Unit,
    onAdd: () -> Unit,
    onMinus: () -> Unit
) {
    var qty by remember { mutableStateOf(0) }
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE6ECEF), RoundedCornerShape(16.dp))
                .padding(10.dp)
        ) {
            // ✅ Image + heart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F6))
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // 💚 Heart (super clean + compact)
                val heartTint by animateColorAsState(
                    targetValue = if (isFavorite) Color(0xFF118B3C) else Color(0xFF8FA2AA)
                )
                val heartBg by animateColorAsState(
                    targetValue = if (isFavorite) Color(0x22118B3C) else Color(0x88FFFFFF)
                )
                val heartScale by animateFloatAsState(
                    targetValue = if (isFavorite) 1.1f else 1f
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(22.dp) // ✅ smaller compact circle
                        .clip(CircleShape)
                        .background(heartBg)
                        .border(0.4.dp, Color(0xFFE6ECEF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            onFavorite()
                        },
                        modifier = Modifier.size(18.dp), // tighter fit to icon
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                        content = {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = heartTint,
                                modifier = Modifier.scale(heartScale)
                            )
                        }
                    )
                }
            }

            // ✅ Product info
            Spacer(Modifier.height(10.dp))
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF0E1F22),
                maxLines = 2
            )
            Spacer(Modifier.height(2.dp))
            Text(weight, fontSize = 12.sp, color = Color(0xFF6B7D85))
            Spacer(Modifier.height(2.dp))
            Text("$sold items sold", fontSize = 12.sp, color = Color(0xFF118B3C))
            Spacer(Modifier.height(8.dp))
            Text(
                text = price,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color(0xFF0E1F22)
            )

            // ✅ Stepper
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFFE6ECEF), RoundedCornerShape(10.dp))
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        if (qty > 0) {
                            qty -= 1
                            onMinus()
                        }
                    },
                    modifier = Modifier.width(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("–", color = Color(0xFF6B7D85), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = qty.toString(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF0E1F22)
                )

                TextButton(
                    onClick = {
                        qty += 1
                        onAdd()
                    },
                    modifier = Modifier.width(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+", color = Color(0xFF118B3C), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
