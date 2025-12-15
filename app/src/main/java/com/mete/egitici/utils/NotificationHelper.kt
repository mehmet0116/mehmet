package com.mete.egitici.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mete.egitici.R

/**
 * Manager for showing notifications and celebrations when achievements are unlocked
 */
class NotificationHelper(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID = "mete_egitici_channel"
        private const val CHANNEL_NAME = "Mete Eğitici Bildirimleri"
        private const val CHANNEL_DESCRIPTION = "Başarılar ve günlük görevler için bildirimler"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showAchievementUnlockedNotification(achievementName: String, points: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🏆 Başarı Açıldı!")
            .setContentText("$achievementName (+$points puan)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
    
    fun showDailyChallengeCompletedNotification(challengeName: String, points: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🎯 Günlük Görev Tamamlandı!")
            .setContentText("$challengeName (+$points puan)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
    
    fun showLevelUpNotification(level: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🌟 Seviye Atladın!")
            .setContentText("Yeni seviye: $level")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
    
    fun showDailyReminderNotification() {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("📚 Öğrenme Zamanı!")
            .setContentText("Bugünkü görevini tamamlamaya ne dersin?")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
        
        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }
    }
}
