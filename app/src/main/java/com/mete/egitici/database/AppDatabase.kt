package com.mete.egitici.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProgressEntity::class,
        AchievementEntity::class,
        DailyChallengeEntity::class,
        UserStatisticsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userProgressDao(): UserProgressDao
    abstract fun achievementDao(): AchievementDao
    abstract fun dailyChallengeDao(): DailyChallengeDao
    abstract fun userStatisticsDao(): UserStatisticsDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mete_database"
                )
                .fallbackToDestructiveMigration() // Allow destructive migration for now
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
