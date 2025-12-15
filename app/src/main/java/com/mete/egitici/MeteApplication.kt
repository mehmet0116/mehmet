package com.mete.egitici

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MeteApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize app-wide settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        
        // Initialize database, preferences, etc.
    }
}
