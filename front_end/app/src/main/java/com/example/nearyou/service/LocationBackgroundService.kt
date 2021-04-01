package com.example.nearyou.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.nearyou.model.Permission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class LocationBackgroundService : Service() {
    companion object {
        var isRunning = false
            private set

        private const val INTERVAL_LOCATION_REQUEST: Long = 60
        private const val FASTEST_INTERVAL_LOCATION_REQUEST: Long = 30
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground()
        else
            startForeground(2, Notification())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager =
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)!!
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        startForeground(2, notification)
    }

    @SuppressLint("MissingPermission") // Control made
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (Permission.isPermissionsAllowed(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
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
                    // TODO : Send data in database
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

        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, RestarterLocationBackgroundService::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}