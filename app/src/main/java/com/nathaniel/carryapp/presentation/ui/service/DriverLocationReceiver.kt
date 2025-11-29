    package com.nathaniel.carryapp.presentation.ui.service

    import android.content.BroadcastReceiver
    import android.content.Context
    import android.content.Intent
    import android.os.Build
    import androidx.annotation.RequiresApi
    import androidx.core.content.ContextCompat
    import timber.log.Timber

    class DriverLocationReceiver : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context, intent: Intent) {
            val serviceIntent = Intent(context, DriverLocationService::class.java)
            Timber.d("🔥 DriverLocationReceiver triggered")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }