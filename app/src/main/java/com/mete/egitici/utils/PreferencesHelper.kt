package com.mete.egitici.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("MetePrefs", Context.MODE_PRIVATE)
    
    fun saveString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
    
    fun getString(key: String, default: String = ""): String {
        return prefs.getString(key, default) ?: default
    }
    
    fun saveInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
    
    fun getInt(key: String, default: Int = 0): Int {
        return prefs.getInt(key, default)
    }
    
    fun saveBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
    
    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(key, default)
    }
}
