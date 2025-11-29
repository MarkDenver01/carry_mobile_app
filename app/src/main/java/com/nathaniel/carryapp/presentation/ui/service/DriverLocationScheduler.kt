package com.nathaniel.carryapp.presentation.ui.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

object DriverLocationScheduler {

    fun start(context: Context) {
        val intent = Intent(context, DriverLocationReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context,
            101,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarm = context.getSystemService(AlarmManager::class.java)

        alarm.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10_000, // first trigger after 5 seconds
            30_000L, // every 10 seconds for testing
            pending
        )
    }

    fun stop(context: Context) {
        val intent = Intent(context, DriverLocationReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarm = context.getSystemService(AlarmManager::class.java)
        alarm.cancel(pending)
    }
}
