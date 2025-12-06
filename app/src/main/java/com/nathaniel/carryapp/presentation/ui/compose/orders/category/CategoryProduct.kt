package com.nathaniel.carryapp.presentation.ui.compose.orders.category

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nathaniel.carryapp.domain.model.ProductRack
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ProductCard

@Composable
fun CategoryProduct(
    racks: List<ProductRack>,
    onProductClick: (Long) -> Unit,
    onAdd: (Long) -> Unit,
    onMinus: (Long) -> Unit
) {
    if (racks.isEmpty()) return

    var selectedIndex by remember { mutableStateOf(0) }
    val cardHeight = LocalConfiguration.current.screenWidthDp.dp * 0.90f

    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {

        /* ============================================================
           LEFT CATEGORY LIST — BALANCED WIDTH
           Now uses weight(0.28f) instead of fixed 125.dp
        ============================================================ */
        LazyColumn(
            modifier = Modifier
                .weight(0.28f)   // ← 28% of screen width
                .fillMaxHeight()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Top
        ) {
            items(racks.size) { index ->

                val rack = racks[index]
                val iconUrl = rack.products.firstOrNull()?.imageUrl
                val isSelected = selectedIndex == index

                val bgColor by animateColorAsState(
                    if (isSelected) Color(0xFF0F8B3C).copy(alpha = 0.15f)
                    else Color.White
                )
                val textColor by animateColorAsState(
                    if (isSelected) Color(0xFF0F8B3C) else Color.DarkGray
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedIndex = index }
                        .background(bgColor, MaterialTheme.shapes.medium)
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Green indicator bar
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(50.dp)
                            .background(
                                if (isSelected) Color(0xFF0F8B3C) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )

                    // Centered icon + text column
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()              // important for centering
                            .padding(horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            model = iconUrl,
                            contentDescription = rack.title,
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.White, CircleShape)
                                .padding(6.dp)
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = rack.title,
                            color = textColor,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Spacer(Modifier.width(4.dp))

        /* ============================================================
//           RIGHT PRODUCT GRID — BALANCED WIDTH
           Uses weight(0.72f)
        ============================================================ */
        val selectedRack = racks[selectedIndex]

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(0.72f)   // ← takes remaining width
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(selectedRack.products, key = { it.id }) { p ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()                        // responsive width
                        .background(Color.White, RoundedCornerShape(16.dp))
                ) {
                    ProductCard(
                        cardHeight = 320.dp,
                        imageHeight = 120.dp,
                        nameMaxLines = 1,
                        imageUrl = p.imageUrl,
                        name = p.name,
                        weight = p.weight,
                        sold = p.sold,
                        price = p.price,
                        expiryDate = p.expiryDate,
                        onFavorite = {},
                        onAdd = { onAdd(p.id) },
                        onMinus = { onMinus(p.id) },
                        onDeduct = {},
                        onRestore = {},
                        onDetailClick = { onProductClick(p.id) }
                    )
                }
            }
        }
    }
}
