package com.nathaniel.carryapp.presentation.ui.compose.dashboard.sidemenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.enum.BadgeStatus
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.DashboardViewModel
import com.nathaniel.carryapp.presentation.ui.compose.membership.apply.MembershipInfoParagraph
import com.nathaniel.carryapp.presentation.ui.compose.membership.suki_badge.VerifiedSukiCard
import com.nathaniel.carryapp.presentation.utils.responsiveDp
import com.nathaniel.carryapp.presentation.utils.responsiveSp

@Composable
fun DashboardDrawerContent(
    viewModel: DashboardViewModel,
    onCloseDrawer: () -> Unit
) {
    val pushNotificationsEnabled by viewModel.pushNotificationsEnabled.collectAsState()
    var showPreferences by remember { mutableStateOf(false) }

    val padding = responsiveDp(16f)
    val logoSize = responsiveDp(100f)
    val iconSpacing = responsiveDp(12f)
    val sectionSpacing = responsiveDp(24f)
    val dividerSpacing = responsiveDp(8f)
    val innerPaddingStart = responsiveDp(48f)
    val textSize = responsiveSp(16f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(padding)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_final),
            contentDescription = "Logo",
            modifier = Modifier
                .size(logoSize)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(sectionSpacing))

        VerifiedSukiCard(
            status = BadgeStatus.NOT_MEMBER,
            viewModel = viewModel
        )
        Divider()
        DrawerItem(
            "My Account",
            Icons.Default.Person,
            textSize = textSize,
            onClick = {
                viewModel.onDisplayProfile()
            })
        DrawerItem(
            "Change Password",
            Icons.Default.Lock,
            textSize = textSize,
            onClick = {
                viewModel.onChangePassword()
            })
        DrawerItem("Address Book", Icons.Default.LocationOn, textSize = textSize)
        DrawerItem(
            "My Voucher",
            Icons.Default.Star,
            textSize = textSize,
            onClick = {
                viewModel.onVoucher()
            })

        Divider(modifier = Modifier.padding(vertical = dividerSpacing))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPreferences = !showPreferences }
                .padding(vertical = dividerSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Settings, contentDescription = null)
            Spacer(modifier = Modifier.width(iconSpacing))
            Text("Preferences", fontSize = textSize, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (showPreferences) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Toggle"
            )
        }

        if (showPreferences) {
            DrawerItem("Support & Feedbacks", Icons.Default.ThumbUp, textSize = textSize)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = innerPaddingStart, top = dividerSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Push Notifications", modifier = Modifier.weight(1f), fontSize = textSize)
                Switch(
                    checked = pushNotificationsEnabled,
                    onCheckedChange = { viewModel.onTogglePushNotifications(it) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Divider()

        DrawerItem(
            label = "Sign Out",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = {
                viewModel.onSignOut()
                onCloseDrawer()
            },
            iconTint = Color.Red,
            textColor = Color.Red,
            textSize = textSize
        )
    }
}
