package com.mete.egitici.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_statistics")
data class UserStatisticsEntity(
    @PrimaryKey
    val userId: String = "default_user",
    val totalPoints: Int = 0,
    val totalGamesPlayed: Int = 0,
    val totalGamesWon: Int = 0,
    val totalLessonsCompleted: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalTimeSpent: Long = 0,
    val averageScore: Float = 0f,
    val perfectScores: Int = 0,
    val achievementsUnlocked: Int = 0,
    val lastPlayedDate: Long = 0,
    val favoriteCategory: String = "",
    val level: Int = 1,
    val experiencePoints: Int = 0
)
