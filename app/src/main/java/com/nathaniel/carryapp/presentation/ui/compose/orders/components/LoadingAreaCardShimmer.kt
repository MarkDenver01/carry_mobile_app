package com.nathaniel.carryapp.presentation.ui.compose.orders.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingAreaCardShimmer() {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(52.dp),
        color = Color(0xFFF2F2F2),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {}
    Spacer(Modifier.height(12.dp))
}