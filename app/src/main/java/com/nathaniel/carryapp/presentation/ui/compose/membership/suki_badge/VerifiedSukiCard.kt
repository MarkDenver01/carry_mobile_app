package com.nathaniel.carryapp.presentation.ui.compose.membership.suki_badge

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.enum.BadgeStatus
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.DashboardViewModel
import com.nathaniel.carryapp.presentation.utils.badgeIconForStatus

@Composable
fun VerifiedSukiCard(
    status: BadgeStatus,
    points: Int = 0,
    viewModel: DashboardViewModel
) {
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val interactionSource = remember { MutableInteractionSource() }

    // Dynamic data based on status
    val (color, title, subtitle) = when (status) {
        BadgeStatus.VERIFIED -> Triple(Color(0xFF2E7D32), "Verified Suki Member", "($points points earned)")
        BadgeStatus.EXPIRED -> Triple(Color(0xFFD32F2F), "Membership Expired", "Renew now to earn points")
        BadgeStatus.PENDING -> Triple(Color(0xFFFFA000), "Pending Verification", "Awaiting approval")
        BadgeStatus.NOT_MEMBER -> Triple(Color.Gray, "Not a Member", "Apply now to become one")
    }

    val iconRes = badgeIconForStatus(status)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = {
                    when (status) {
                        BadgeStatus.NOT_MEMBER -> viewModel.onMembership()
                        BadgeStatus.VERIFIED -> viewModel.onVerifiedBadge()
                        else -> {}
                       // BadgeStatus.EXPIRED -> viewModel.onRenewMembership()
                       // BadgeStatus.PENDING -> viewModel.onPendingInfo()
                    }
                }
            )
            .padding(horizontal = spacing.md, vertical = spacing.lsm)
    ) {
        Box(
            modifier = Modifier
                .size(sizes.iconSize * 1.4f)
                .clip(CircleShape)
                .border(2.dp, color, CircleShape)
                .background(color.copy(alpha = 0.95f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = "Badge Icon",
                tint = Color.White,
                modifier = Modifier.size(sizes.iconSize * 0.85f)
            )
        }

        Spacer(modifier = Modifier.width(spacing.md))

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}
