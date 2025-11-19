package com.nathaniel.carryapp.presentation.ui.compose.orders.location

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.presentation.ui.compose.orders.location.IconMapper.provinceIcon

private val Green = Color(0xFF2E7D32)
private val GrayBorder = Color(0xFFE0E0E0)

@Composable
fun AreaRow(
    title: String,
    isSelected: Boolean,
    isMain: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isMain || isSelected) Green else GrayBorder
    )

    val iconSize = if (isMain) 50.dp else 38.dp
    val rowHeight = if (isMain) 80.dp else 70.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(rowHeight)
            .border(BorderStroke(1.dp, borderColor))
            .background(Color.White)
            .padding(horizontal = 16.dp) // No vertical padding
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = provinceIcon(title)),
                contentDescription = title,
                tint = Color.Unspecified,
                modifier = Modifier.size(iconSize)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = if (isMain) 22.sp else 20.sp,
                fontWeight = if (isMain) FontWeight.SemiBold else FontWeight.Medium,
                color = Green
            )
        }
    }
}
