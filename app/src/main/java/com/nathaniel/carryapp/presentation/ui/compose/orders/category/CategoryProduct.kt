package com.nathaniel.carryapp.presentation.ui.compose.orders.category

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nathaniel.carryapp.domain.model.ProductRack
import com.nathaniel.carryapp.presentation.ui.compose.orders.widgets.ProductCard

@Composable
fun CategoryProduct(
    racks: List<ProductRack>,              // filtered (1 category)
    originalRacks: List<ProductRack>,      // full category list (left menu)
    selectedCategory: String,              // current selected
    onCategorySelected: (String) -> Unit,  // callback
    onProductClick: (Long) -> Unit,
    onAdd: (Long) -> Unit,
    onMinus: (Long) -> Unit
) {
    if (racks.isEmpty()) return

    // ⭐ Auto-locate selected category index
    var selectedIndex by remember(selectedCategory) {
        mutableStateOf(
            originalRacks.indexOfFirst { it.title == selectedCategory }
                .coerceAtLeast(0)
        )
    }

    val selectedRack = racks.firstOrNull() ?: return

    Row(
        modifier = Modifier.fillMaxSize()
    ) {

        // ---------------------------------------------------------
        // LEFT MENU (All categories)
        // ---------------------------------------------------------
        LazyColumn(
            modifier = Modifier
                .weight(0.28f)
                .fillMaxHeight()
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Top
        ) {
            items(originalRacks.size) { index ->

                val rack = originalRacks[index]
                val isSelected = selectedIndex == index

                val bgColor by animateColorAsState(
                    if (isSelected) Color(0xFF0F8B3C).copy(alpha = 0.15f)
                    else Color.White
                )

                val textColor by animateColorAsState(
                    if (isSelected) Color(0xFF0F8B3C)
                    else Color.DarkGray
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedIndex = index
                            onCategorySelected(rack.title)
                        }
                        .background(bgColor, MaterialTheme.shapes.medium)
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Indicator bar
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(50.dp)
                            .background(
                                if (isSelected) Color(0xFF0F8B3C) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = rack.products.firstOrNull()?.imageUrl,
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

        // ---------------------------------------------------------
        // RIGHT PRODUCT GRID
        // ---------------------------------------------------------

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(0.72f)
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(selectedRack.products, key = { it.id }) { p ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
