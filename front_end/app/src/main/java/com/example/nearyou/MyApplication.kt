package com.example.nearyou

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.nearyou.model.database.DatabaseHandler
import java.util.*

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Update theme
        val currentTheme = DatabaseHandler(this).getConf("theme")
        if (currentTheme == "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Update language
        val conf: Configuration = resources.configuration
        conf.setLocale(Locale(DatabaseHandler(this).getConf("language")))
        resources.updateConfiguration(conf, resources.displayMetrics)
    }
}