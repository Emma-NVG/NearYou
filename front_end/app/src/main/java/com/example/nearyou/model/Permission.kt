package com.example.nearyou.model

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permission {
    companion object {
        fun isPermissionsAllowed(permissions: Array<String>, ctx: Context): Boolean =
                permissions.none { ContextCompat.checkSelfPermission(ctx, it) != PackageManager.PERMISSION_GRANTED }

        fun shouldShowRequestPermissionRationale(permissions: Array<String>, ctx: Activity): Boolean =
                permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(ctx, it) }
    }
}