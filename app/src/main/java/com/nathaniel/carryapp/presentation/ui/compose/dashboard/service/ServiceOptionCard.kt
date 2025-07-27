package com.nathaniel.carryapp.presentation.ui.compose.dashboard.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nathaniel.carryapp.presentation.utils.responsiveDp
import com.nathaniel.carryapp.presentation.utils.responsiveSp

@Composable
fun ServiceOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    val iconSize = responsiveDp(78f)
    val padding = responsiveDp(16f)
    val cardCornerRadius = responsiveDp(24f)
    val titleFontSize = responsiveSp(16f)
    val subtitleFontSize = responsiveSp(13f)

    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        shape = RoundedCornerShape(cardCornerRadius),
        color = Color(0xFFE6FF00),
        tonalElevation = 0.dp,
        shadowElevation = responsiveDp(4f),
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = titleFontSize
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF2E7D32),
                    fontSize = subtitleFontSize
                )
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "$title Icon",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
