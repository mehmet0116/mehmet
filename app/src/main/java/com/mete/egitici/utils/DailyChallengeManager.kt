package com.mete.egitici.utils

import com.mete.egitici.database.DailyChallengeDao
import com.mete.egitici.database.DailyChallengeEntity
import com.mete.egitici.models.ChallengeDifficulty
import com.mete.egitici.models.ChallengeTaskType
import java.util.*

/**
 * Manager for generating and handling daily challenges
 */
class DailyChallengeManager(
    private val dailyChallengeDao: DailyChallengeDao
) {
    
    suspend fun generateDailyChallenge() {
        val today = getTodayStartTime()
        val existingChallenge = dailyChallengeDao.getTodayChallenge(today)
        
        if (existingChallenge == null) {
            val newChallenge = createRandomChallenge(today)
            dailyChallengeDao.insertChallenge(newChallenge)
            
            // Clean up old challenges (older than 30 days)
            val thirtyDaysAgo = today - (30 * 24 * 60 * 60 * 1000)
            dailyChallengeDao.deleteOldChallenges(thirtyDaysAgo)
        }
    }
    
    private fun getTodayStartTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    private fun createRandomChallenge(date: Long): DailyChallengeEntity {
        val challenges = getAllPossibleChallenges()
        val randomChallenge = challenges.random()
        
        return DailyChallengeEntity(
            id = "challenge_${date}_${Random().nextInt(10000)}",
            title = randomChallenge.title,
            description = randomChallenge.description,
            category = randomChallenge.category,
            difficulty = randomChallenge.difficulty.name,
            rewardPoints = when(randomChallenge.difficulty) {
                ChallengeDifficulty.EASY -> 10
                ChallengeDifficulty.MEDIUM -> 20
                ChallengeDifficulty.HARD -> 30
            },
            date = date,
            taskType = randomChallenge.taskType.name,
            targetValue = randomChallenge.targetValue
        )
    }
    
    private fun getAllPossibleChallenges(): List<ChallengeTemplate> {
        return listOf(
            // Easy Challenges
            ChallengeTemplate(
                "Günün Dersi",
                "Bugün 1 ders tamamla",
                "Eğitim",
                ChallengeDifficulty.EASY,
                ChallengeTaskType.COMPLETE_LESSONS,
                1
            ),
            ChallengeTemplate(
                "Küçük Hedef",
                "10 puan kazan",
                "Genel",
                ChallengeDifficulty.EASY,
                ChallengeTaskType.SCORE_POINTS,
                10
            ),
            ChallengeTemplate(
                "Oyun Zamanı",
                "1 oyun kazan",
                "Oyunlar",
                ChallengeDifficulty.EASY,
                ChallengeTaskType.WIN_GAMES,
                1
            ),
            
            // Medium Challenges
            ChallengeTemplate(
                "Aktif Öğrenci",
                "Bugün 3 ders tamamla",
                "Eğitim",
                ChallengeDifficulty.MEDIUM,
                ChallengeTaskType.COMPLETE_LESSONS,
                3
            ),
            ChallengeTemplate(
                "Puan Avcısı",
                "50 puan kazan",
                "Genel",
                ChallengeDifficulty.MEDIUM,
                ChallengeTaskType.SCORE_POINTS,
                50
            ),
            ChallengeTemplate(
                "Oyun Ustası",
                "3 oyun kazan",
                "Oyunlar",
                ChallengeDifficulty.MEDIUM,
                ChallengeTaskType.WIN_GAMES,
                3
            ),
            ChallengeTemplate(
                "Mükemmellik",
                "1 mükemmel skor al",
                "Başarı",
                ChallengeDifficulty.MEDIUM,
                ChallengeTaskType.PERFECT_SCORE,
                1
            ),
            
            // Hard Challenges
            ChallengeTemplate(
                "Süper Öğrenci",
                "Bugün 5 ders tamamla",
                "Eğitim",
                ChallengeDifficulty.HARD,
                ChallengeTaskType.COMPLETE_LESSONS,
                5
            ),
            ChallengeTemplate(
                "Yüksek Hedef",
                "100 puan kazan",
                "Genel",
                ChallengeDifficulty.HARD,
                ChallengeTaskType.SCORE_POINTS,
                100
            ),
            ChallengeTemplate(
                "Şampiyon",
                "5 oyun kazan",
                "Oyunlar",
                ChallengeDifficulty.HARD,
                ChallengeTaskType.WIN_GAMES,
                5
            ),
            ChallengeTemplate(
                "Hatasız Performans",
                "3 mükemmel skor al",
                "Başarı",
                ChallengeDifficulty.HARD,
                ChallengeTaskType.PERFECT_SCORE,
                3
            )
        )
    }
    
    suspend fun updateChallengeProgress(challengeId: String, progress: Int) {
        val challenge = dailyChallengeDao.getChallengeById(challengeId)
        challenge?.let {
            if (progress >= it.targetValue && !it.isCompleted) {
                dailyChallengeDao.completeChallenge(challengeId, System.currentTimeMillis())
            } else {
                dailyChallengeDao.updateProgress(challengeId, progress)
            }
        }
    }
    
    private data class ChallengeTemplate(
        val title: String,
        val description: String,
        val category: String,
        val difficulty: ChallengeDifficulty,
        val taskType: ChallengeTaskType,
        val targetValue: Int
    )
}
