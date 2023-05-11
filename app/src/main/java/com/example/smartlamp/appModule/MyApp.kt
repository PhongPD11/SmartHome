package com.example.smartlamp.appModule

import android.app.Application
import android.content.res.Configuration
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        setAppTheme(Configuration.UI_MODE_NIGHT_NO)
    }

    private fun setAppTheme(themeMode: Int) {
        val config = Configuration(resources.configuration)
        config.uiMode = themeMode
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}