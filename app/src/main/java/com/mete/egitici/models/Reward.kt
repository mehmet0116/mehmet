package com.mete.egitici.models

/**
 * Reward model for user incentives
 */
data class Reward(
    val id: String,
    val name: String,
    val description: String,
    val rewardType: RewardType,
    val icon: String,
    val value: Int,
    val isCollected: Boolean = false,
    val collectedDate: Long? = null
)

enum class RewardType {
    STAR,          // Yıldız
    TROPHY,        // Kupa
    MEDAL,         // Madalya
    BADGE,         // Rozet
    AVATAR,        // Avatar
    THEME,         // Tema
    POINTS         // Puan
}

/**
 * User statistics for comprehensive tracking
 */
data class UserStatistics(
    val totalPoints: Int = 0,
    val totalGamesPlayed: Int = 0,
    val totalGamesWon: Int = 0,
    val totalLessonsCompleted: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalTimeSpent: Long = 0, // in milliseconds
    val averageScore: Float = 0f,
    val perfectScores: Int = 0,
    val achievementsUnlocked: Int = 0,
    val lastPlayedDate: Long = 0,
    val favoriteCategory: String = "",
    val level: Int = 1,
    val experiencePoints: Int = 0
)
