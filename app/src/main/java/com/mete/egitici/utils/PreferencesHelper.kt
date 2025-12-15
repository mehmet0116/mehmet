package com.mete.egitici.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("MetePrefs", Context.MODE_PRIVATE)
    
    companion object {
        // Keys
        private const val KEY_SOUND_EFFECTS = "sound_effects"
        private const val KEY_BACKGROUND_MUSIC = "background_music"
        private const val KEY_VOLUME = "volume"
        private const val KEY_ANIMATIONS = "animations"
        private const val KEY_PARTICLE_EFFECTS = "particle_effects"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_TEXT_SIZE = "text_size"
        private const val KEY_ACCESSIBILITY_MODE = "accessibility_mode"
        private const val KEY_HIGH_CONTRAST = "high_contrast"
        private const val KEY_TIME_LIMIT = "time_limit_enabled"
        private const val KEY_DAILY_TIME_LIMIT = "daily_time_limit"
        private const val KEY_NOTIFICATIONS = "notifications"
        private const val KEY_DAILY_REMINDERS = "daily_reminders"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_AGE = "user_age"
        private const val KEY_AVATAR_ID = "avatar_id"
    }
    
    // Sound Settings
    fun isSoundEffectsEnabled(): Boolean = getBoolean(KEY_SOUND_EFFECTS, true)
    fun setSoundEffectsEnabled(enabled: Boolean) = saveBoolean(KEY_SOUND_EFFECTS, enabled)
    
    fun isBackgroundMusicEnabled(): Boolean = getBoolean(KEY_BACKGROUND_MUSIC, true)
    fun setBackgroundMusicEnabled(enabled: Boolean) = saveBoolean(KEY_BACKGROUND_MUSIC, enabled)
    
    fun getVolume(): Float = prefs.getFloat(KEY_VOLUME, 0.7f)
    fun setVolume(volume: Float) = prefs.edit().putFloat(KEY_VOLUME, volume).apply()
    
    // Animation Settings
    fun isAnimationsEnabled(): Boolean = getBoolean(KEY_ANIMATIONS, true)
    fun setAnimationsEnabled(enabled: Boolean) = saveBoolean(KEY_ANIMATIONS, enabled)
    
    fun isParticleEffectsEnabled(): Boolean = getBoolean(KEY_PARTICLE_EFFECTS, true)
    fun setParticleEffectsEnabled(enabled: Boolean) = saveBoolean(KEY_PARTICLE_EFFECTS, enabled)
    
    // Display Settings
    fun isDarkModeEnabled(): Boolean = getBoolean(KEY_DARK_MODE, false)
    fun setDarkModeEnabled(enabled: Boolean) = saveBoolean(KEY_DARK_MODE, enabled)
    
    fun getTextSize(): Int = getInt(KEY_TEXT_SIZE, 1) // 0=Small, 1=Medium, 2=Large
    fun setTextSize(size: Int) = saveInt(KEY_TEXT_SIZE, size)
    
    // Accessibility Settings
    fun isAccessibilityModeEnabled(): Boolean = getBoolean(KEY_ACCESSIBILITY_MODE, false)
    fun setAccessibilityModeEnabled(enabled: Boolean) = saveBoolean(KEY_ACCESSIBILITY_MODE, enabled)
    
    fun isHighContrastEnabled(): Boolean = getBoolean(KEY_HIGH_CONTRAST, false)
    fun setHighContrastEnabled(enabled: Boolean) = saveBoolean(KEY_HIGH_CONTRAST, enabled)
    
    // Parental Control Settings
    fun isTimeLimitEnabled(): Boolean = getBoolean(KEY_TIME_LIMIT, false)
    fun setTimeLimitEnabled(enabled: Boolean) = saveBoolean(KEY_TIME_LIMIT, enabled)
    
    fun getDailyTimeLimit(): Int = getInt(KEY_DAILY_TIME_LIMIT, 60) // Default 60 minutes
    fun setDailyTimeLimit(minutes: Int) = saveInt(KEY_DAILY_TIME_LIMIT, minutes)
    
    // Notification Settings
    fun isNotificationsEnabled(): Boolean = getBoolean(KEY_NOTIFICATIONS, true)
    fun setNotificationsEnabled(enabled: Boolean) = saveBoolean(KEY_NOTIFICATIONS, enabled)
    
    fun isDailyRemindersEnabled(): Boolean = getBoolean(KEY_DAILY_REMINDERS, true)
    fun setDailyRemindersEnabled(enabled: Boolean) = saveBoolean(KEY_DAILY_REMINDERS, enabled)
    
    // User Profile
    fun isFirstLaunch(): Boolean = getBoolean(KEY_FIRST_LAUNCH, true)
    fun setFirstLaunch(isFirst: Boolean) = saveBoolean(KEY_FIRST_LAUNCH, isFirst)
    
    fun getUserName(): String = getString(KEY_USER_NAME, "Küçük Öğrenci")
    fun setUserName(name: String) = saveString(KEY_USER_NAME, name)
    
    fun getUserAge(): Int = getInt(KEY_USER_AGE, 5)
    fun setUserAge(age: Int) = saveInt(KEY_USER_AGE, age)
    
    fun getAvatarId(): Int = getInt(KEY_AVATAR_ID, 0)
    fun setAvatarId(id: Int) = saveInt(KEY_AVATAR_ID, id)
    
    // Generic helpers
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
    
    fun saveLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }
    
    fun getLong(key: String, default: Long = 0L): Long {
        return prefs.getLong(key, default)
    }
}

