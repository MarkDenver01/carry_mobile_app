package com.nathaniel.carryapp.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.enum.BadgeStatus

fun Color.darken(factor: Float = 0.85f): Color {
    return Color(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha
    )
}

@Composable
fun badgeIconForStatus(status: BadgeStatus): Int {
    return when (status) {
        BadgeStatus.VERIFIED -> R.drawable.ic_verified_badge
        BadgeStatus.EXPIRED -> R.drawable.ic_expired_badge
        BadgeStatus.PENDING -> R.drawable.ic_pending_badge
        BadgeStatus.NOT_MEMBER -> R.drawable.ic_not_member_badge
    }
}