package com.nathaniel.carryapp.presentation.ui.compose.orders.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.presentation.ui.compose.orders.shopping.DummyCartItem

@Composable
fun CartItemCard(
    item: DummyCartItem,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    onAddRecommended: () -> Unit,
    onViewRecommended: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text("₱${"%,.2f".format(item.price)}", fontWeight = FontWeight.Bold)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray)
                }
                IconButton(onClick = onAddRecommended) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Add Recommended", tint = Color.Gray)
                }
                IconButton(onClick = onViewRecommended) {
                    Icon(Icons.Default.Star, contentDescription = "View Recommended", tint = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDecrease,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF2E7D32),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32))
                ) {
                    Text("-", color = Color.White, fontSize = 20.sp)
                }

                Text(
                    text = quantity.toString(),
                    color = Color(0xFF2E7D32),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                OutlinedButton(
                    onClick = onIncrease,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF2E7D32),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32))
                ) {
                    Text("+", color = Color.White, fontSize = 20.sp)
                }
            }

            Text("Stock Available: ${item.stock}", fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
