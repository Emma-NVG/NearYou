package com.example.nearyou.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.example.nearyou.R
import com.example.nearyou.model.Permission
import com.example.nearyou.model.location.Location
import com.example.nearyou.model.user.UserDAO
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocationBackgroundService : Service() {
    private lateinit var notificationManager: NotificationManager

    companion object {
        var isRunning = false
            private set

        private const val NOTIFICATION_CHANNEL_ID = "com.example.nearyou.service.location"

        private const val INTERVAL_LOCATION_REQUEST: Long = 3 * 60 * 1000
        private const val FASTEST_INTERVAL_LOCATION_REQUEST: Long = 60 * 1000
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize the notification manager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startService()
    }

    private fun startService() {
        // 0. Get data
        val mainNotificationText = getString(R.string.notification_location)
        val titleText = "NearYou"

        // 1. Create Notification Channel for O+ and beyond devices (26+).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_NONE)

            notificationManager.createNotificationChannel(notificationChannel)
        }

        // 2. Build the BIG_TEXT_STYLE.
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        // 3. Build and issue the notification.
        // Notification Channel Id is ignored for Android pre O (26).
        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        val notification = notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVibrate(longArrayOf(0L))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(R.drawable.ic_menu_logout, "Stop", null)
            .build()

        startForeground(2, notification)
    }

    @SuppressLint("MissingPermission") // Control made
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (Permission.isPermissionsAllowed(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                this
            )
        ) {
            isRunning = true

            val locationRequest = LocationRequest.create()
            locationRequest.interval = INTERVAL_LOCATION_REQUEST
            locationRequest.fastestInterval = FASTEST_INTERVAL_LOCATION_REQUEST
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val mLocationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (UserDAO.user != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val androidLocation = locationResult.lastLocation
                            UserDAO.addLocation(
                                Location(
                                    androidLocation.latitude,
                                    androidLocation.longitude
                                )
                            )
                        }
                    }
                }
            }

            LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper())
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        isRunning = false

        if (UserDAO.user != null) {
            val broadcastIntent = Intent()
            broadcastIntent.action = "restartservice"
            broadcastIntent.setClass(this, RestarterLocationBackgroundService::class.java)
            this.sendBroadcast(broadcastIntent)
        }
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}