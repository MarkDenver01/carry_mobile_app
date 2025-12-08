package com.nathaniel.carryapp.presentation.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.enum.BadgeStatus
import com.nathaniel.carryapp.presentation.theme.AppSpacing

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

fun getAppVersionName(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "Unknown"
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }
}

fun shouldShowPromoToday(context: Context): Boolean {
    val prefs = context.getSharedPreferences("promo_prefs", Context.MODE_PRIVATE)

    val lastShown = prefs.getLong("last_shown_time", 0L)
    val currentTime = System.currentTimeMillis()

    val oneDayMillis = 24 * 60 * 60 * 1000 // ✅ 24 hours

    return if (currentTime - lastShown >= oneDayMillis) {
        prefs.edit().putLong("last_shown_time", currentTime).apply()
        true // ✅ SHOW PROMO
    } else {
        false // ❌ DO NOT SHOW
    }
}


fun areNotificationsEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun requestPermission(activity: Activity) {
    if (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            1001
        )
    }
}

fun openSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}


