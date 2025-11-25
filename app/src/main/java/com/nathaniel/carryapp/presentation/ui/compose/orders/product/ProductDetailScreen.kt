package com.nathaniel.carryapp.presentation.ui.compose.orders.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.ui.compose.orders.OrderViewModel
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopBottomBar
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopHeader
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ShopSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    imageUrl: String,
    name: String,
    size: String,
    description: String,
    price: String,
    viewModel: OrderViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onAddToCart: (Int) -> Unit = {}
) {
    var qty by remember { mutableStateOf(0) }
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            ShopHeader(
                notifications = 12,
                cartCount = 15,
                onCartClick = {

                },
                onNotificationClick = {

                }
            )
        },
        bottomBar = {
            ShopBottomBar(
                onHome = { viewModel.onHomeClick() },
                onCategories = { viewModel.onCategoriesClick() },
                onReorder = { viewModel.onReorderClick() },
                onAccount = { viewModel.onAccountClick() }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {

            item {
                Column {

                    ShopSearchBar(
                        hint = "I'm looking for…",
                        onSearch = { viewModel.onSearchClick() }
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {

                        // ================================
                        // IMAGE HEADER (INSIDE CARD)
                        // ================================
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                                .background(Color.LightGray)
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                            )

                            // Favorite Button
                            IconButton(
                                onClick = { isFavorite = !isFavorite },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.85f))
                                    .border(1.dp, Color(0xFFE6ECEF), CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) Color(0xFF118B3C) else Color(0xFF8FA2AA),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        // ================================
                        // PRODUCT TITLE
                        // ================================
                        Text(
                            text = name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            lineHeight = 26.sp,
                            color = Color(0xFF0E1F22),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = size,
                            fontSize = 14.sp,
                            color = Color(0xFF6B7D85),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        // ================================
                        // DESCRIPTION
                        // ================================
                        Text(
                            text = description,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF0E1F22),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(Modifier.height(20.dp))

                        // ================================
                        // PRICE
                        // ================================
                        Text(
                            text = price,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 26.sp,
                            color = Color(0xFF118B3C),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(Modifier.height(26.dp))

                        // ================================
                        // QUANTITY STEPPER
                        // ================================
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(55.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFF118B3C), RoundedCornerShape(14.dp))
                                .background(Color.White),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TextButton(
                                onClick = { if (qty > 0) qty-- },
                                modifier = Modifier.width(60.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    "-",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF118B3C)
                                )
                            }

                            Text(
                                text = qty.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0E1F22)
                            )

                            TextButton(
                                onClick = { qty++ },
                                modifier = Modifier.width(60.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    "+",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF118B3C)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
