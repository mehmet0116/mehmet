package com.mete.egitici.models

/**
 * Daily Challenge model for engaging users with fresh content
 */
data class DailyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: ChallengeDifficulty,
    val rewardPoints: Int,
    val date: Long,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null,
    val taskType: ChallengeTaskType,
    val targetValue: Int = 1
)

enum class ChallengeDifficulty {
    EASY,    // Kolay - 10 puan
    MEDIUM,  // Orta - 20 puan
    HARD     // Zor - 30 puan
}

enum class ChallengeTaskType {
    COMPLETE_LESSONS,      // Ders tamamla
    SCORE_POINTS,          // Puan topla
    WIN_GAMES,             // Oyun kazan
    PERFECT_SCORE,         // Mükemmel skor
    TIME_CHALLENGE,        // Süre challenge'ı
    COMBO_CHALLENGE        // Kombo challenge
}
