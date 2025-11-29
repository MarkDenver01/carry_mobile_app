package com.nathaniel.carryapp.presentation.ui.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.nathaniel.carryapp.domain.request.DriverLocationUpdateRequest
import com.nathaniel.carryapp.domain.usecase.UpdateDriverLocationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DriverLocationService : Service() {

    @Inject
    lateinit var updateDriverLocationUseCase: UpdateDriverLocationUseCase

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val handler = Handler(Looper.getMainLooper())

    //    private val UPDATE_INTERVAL = 5 * 60 * 1000L // 5 minutes
    private val UPDATE_INTERVAL = 30 * 1000L // 10 seconds
    private val driverId = "10" // TODO load from session

    override fun onCreate() {
        super.onCreate()


        if (!::fusedLocationClient.isInitialized) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }
        Timber.d("🔥 Service created - fusedLocation initialized")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(1, buildNotification())

        if (!hasLocationPermission()) {
            Timber.e("❌ Location permission missing. Stopping.")
            stopSelf()
            return START_NOT_STICKY
        }

        Timber.d("🚀 Foreground started — STARTING location loop")
        startLocationLoop()

        return START_STICKY
    }

    // ================================================
    // LOCATION LOOP (every 5 mins) — like Grab/Lalamove
    // ================================================
    private fun startLocationLoop() {
        handler.post(object : Runnable {
            override fun run() {
                requestSingleLocation()
                handler.postDelayed(this, UPDATE_INTERVAL)
            }
        })
    }

    // ================================================
    // Get single (freshest) location
    // ================================================
    private fun requestSingleLocation() {
        try {
            val req = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMaxUpdateAgeMillis(0)
                .build()

            fusedLocationClient
                .getCurrentLocation(req.priority, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        Timber.d("📍 Got location: ${location.latitude}, ${location.longitude}")
                        sendLocation(location)
                    } else {
                        Timber.e("⚠ No location returned")
                    }
                }
        } catch (e: SecurityException) {
            Timber.e(e, "❌ Missing location permission")
            stopSelf()
        }
    }

    // ================================================
    // Send location to backend
    // ================================================
    private fun sendLocation(location: android.location.Location) {
        val dto = DriverLocationUpdateRequest(
            driverId = driverId,
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            timestamp = System.currentTimeMillis()
        )

        scope.launch {
            try {
                updateDriverLocationUseCase(dto)
                Timber.d("✅ Sent: ${location.latitude}, ${location.longitude}")
            } catch (e: Exception) {
                Timber.e("❌ Failed sending location: $e")
            }
        }
    }

    // ================================================
    // Stop service
    // ================================================
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        Timber.d("🛑 Service destroyed — loop stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ================================================
    // Helpers
    // ================================================
    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    private fun buildNotification(): Notification {
        val channelId = "driver_location_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Driver Location",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Sharing location")
            .setContentText("Delivery tracking active…")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }
}
