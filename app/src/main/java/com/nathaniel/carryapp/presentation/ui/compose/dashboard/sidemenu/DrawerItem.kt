package com.nathaniel.carryapp.presentation.ui.compose.dashboard.sidemenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.nathaniel.carryapp.presentation.utils.responsiveDp
import com.nathaniel.carryapp.presentation.utils.responsiveSp

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    onClick: (() -> Unit)? = null,
    iconTint: Color = Color.Black,
    textColor: Color = Color.Black,
    textSize: TextUnit = responsiveSp(16f),
    iconSize: Dp = responsiveDp(24f),
    verticalPadding: Dp = responsiveDp(8f),
    horizontalSpacing: Dp = responsiveDp(12f)
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isHovered by remember { mutableStateOf(false) }

    // Track hover enter/exit interactions
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is HoverInteraction.Enter -> isHovered = true
                is HoverInteraction.Exit -> isHovered = false
            }
        }
    }

    val backgroundColor = if (isHovered) Color(0xFFE0E0E0) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .hoverable(interactionSource = interactionSource)
            .clickable(
                enabled = onClick != null,
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick?.invoke()
            }
            .padding(vertical = verticalPadding, horizontal = responsiveDp(16f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(horizontalSpacing))
        Text(
            text = label,
            color = textColor,
            fontSize = textSize
        )
    }
}
