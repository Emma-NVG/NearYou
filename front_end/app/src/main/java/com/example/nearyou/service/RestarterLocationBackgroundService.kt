package com.example.nearyou.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build


class RestarterLocationBackgroundService : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, LocationBackgroundService::class.java))
        } else {
            context.startService(Intent(context, LocationBackgroundService::class.java))
        }
    }
}