package com.nathaniel.carryapp.presentation.ui.compose.navigation

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun fancyBottomNavShape(): Shape {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val scaleFactor = screenWidthDp / 411f // 411 is baseline (Pixel 2 XL, typical design ref)

    val cutoutRadius = 36f * scaleFactor
    val cutoutWidth = cutoutRadius * 2 + (32f * scaleFactor)
    val cutoutDepth = cutoutRadius + (16f * scaleFactor)

    return GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val center = width / 2f

        moveTo(0f, 0f)
        lineTo(center - cutoutWidth / 2f, 0f)

        // Curve up into the cutout
        cubicTo(
            center - cutoutWidth / 2f + cutoutRadius / 2f, 0f,
            center - cutoutRadius, cutoutDepth,
            center, cutoutDepth
        )
        cubicTo(
            center + cutoutRadius, cutoutDepth,
            center + cutoutWidth / 2f - cutoutRadius / 2f, 0f,
            center + cutoutWidth / 2f, 0f
        )

        lineTo(width, 0f)
        lineTo(width, height)
        lineTo(0f, height)
        close()
    }
}
