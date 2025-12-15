package com.mete.egitici.models

/**
 * Achievement/Badge model for tracking user accomplishments
 */
data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val category: AchievementCategory,
    val icon: String,
    val requiredPoints: Int,
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null,
    val rewardPoints: Int = 0
)

enum class AchievementCategory {
    BEGINNER,      // İlk adımlar
    LANGUAGE,      // Dil gelişimi
    MATH,          // Matematik
    COGNITIVE,     // Bilişsel
    CREATIVE,      // Yaratıcılık
    SCIENCE,       // Fen bilgisi
    SOCIAL,        // Sosyal
    GAMES,         // Oyunlar
    MASTER         // Ustalaşma
}
