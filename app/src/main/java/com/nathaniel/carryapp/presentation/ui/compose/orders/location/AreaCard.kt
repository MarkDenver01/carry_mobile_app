package com.nathaniel.carryapp.presentation.ui.compose.orders.location

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment

private val Green = Color(0xFF2E7D32)
private val BorderGray = Color(0xFFE0E0E0)

@Composable
fun AreaCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isMain: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        border = BorderStroke(1.dp, if (isSelected || isMain) Green else BorderGray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = if (isMain) 18.dp else 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Green,
                modifier = Modifier.size(if (isMain) 34.dp else 28.dp)
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = if (isMain) 18.sp else 16.sp,
                fontWeight = if (isMain) FontWeight.SemiBold else FontWeight.Medium,
                color = Green
            )
        }
    }
}