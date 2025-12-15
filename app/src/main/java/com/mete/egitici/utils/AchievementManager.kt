package com.mete.egitici.utils

import android.content.Context
import com.mete.egitici.database.AchievementDao
import com.mete.egitici.database.AchievementEntity
import com.mete.egitici.models.AchievementCategory
import kotlinx.coroutines.flow.first
import java.util.*

/**
 * Manager for handling achievements and unlocking logic
 */
class AchievementManager(
    private val context: Context,
    private val achievementDao: AchievementDao
) {
    
    suspend fun initializeAchievements() {
        // Check if achievements are already initialized
        val existingAchievements = achievementDao.getAllAchievements().first()
        if (existingAchievements.isNotEmpty()) return
        
        // Create default achievements
        val achievements = createDefaultAchievements()
        achievementDao.insertAchievements(achievements)
    }
    
    private fun createDefaultAchievements(): List<AchievementEntity> {
        return listOf(
            // Beginner Achievements
            AchievementEntity(
                id = "first_steps",
                name = "İlk Adımlar",
                description = "İlk dersini tamamla",
                category = AchievementCategory.BEGINNER.name,
                icon = "🎯",
                requiredPoints = 0,
                rewardPoints = 10
            ),
            AchievementEntity(
                id = "early_bird",
                name = "Erken Kuş",
                description = "İlk 50 puan",
                category = AchievementCategory.BEGINNER.name,
                icon = "🐣",
                requiredPoints = 50,
                rewardPoints = 20
            ),
            
            // Language Achievements
            AchievementEntity(
                id = "word_master",
                name = "Kelime Ustası",
                description = "100 kelime öğren",
                category = AchievementCategory.LANGUAGE.name,
                icon = "📚",
                requiredPoints = 100,
                rewardPoints = 50
            ),
            AchievementEntity(
                id = "alphabet_champion",
                name = "Alfabe Şampiyonu",
                description = "Tüm harfleri öğren",
                category = AchievementCategory.LANGUAGE.name,
                icon = "🏆",
                requiredPoints = 150,
                rewardPoints = 75
            ),
            
            // Math Achievements
            AchievementEntity(
                id = "number_wizard",
                name = "Sayı Sihirbazı",
                description = "10 matematik oyunu kazan",
                category = AchievementCategory.MATH.name,
                icon = "🔢",
                requiredPoints = 200,
                rewardPoints = 60
            ),
            AchievementEntity(
                id = "calculation_king",
                name = "Hesaplama Kralı",
                description = "50 toplama işlemi yap",
                category = AchievementCategory.MATH.name,
                icon = "👑",
                requiredPoints = 250,
                rewardPoints = 80
            ),
            
            // Cognitive Achievements
            AchievementEntity(
                id = "memory_master",
                name = "Hafıza Ustası",
                description = "Hafıza oyunlarında 5 mükemmel skor",
                category = AchievementCategory.COGNITIVE.name,
                icon = "🧠",
                requiredPoints = 300,
                rewardPoints = 100
            ),
            AchievementEntity(
                id = "logic_genius",
                name = "Mantık Dehası",
                description = "Tüm mantık bulmacalarını çöz",
                category = AchievementCategory.COGNITIVE.name,
                icon = "💡",
                requiredPoints = 350,
                rewardPoints = 120
            ),
            
            // Creative Achievements
            AchievementEntity(
                id = "little_artist",
                name = "Küçük Sanatçı",
                description = "10 resim yap",
                category = AchievementCategory.CREATIVE.name,
                icon = "🎨",
                requiredPoints = 150,
                rewardPoints = 50
            ),
            AchievementEntity(
                id = "music_maestro",
                name = "Müzik Maestrosu",
                description = "5 melodi oluştur",
                category = AchievementCategory.CREATIVE.name,
                icon = "🎵",
                requiredPoints = 200,
                rewardPoints = 60
            ),
            
            // Master Achievements
            AchievementEntity(
                id = "perfect_week",
                name = "Mükemmel Hafta",
                description = "7 gün üst üste oyna",
                category = AchievementCategory.MASTER.name,
                icon = "⭐",
                requiredPoints = 500,
                rewardPoints = 200
            ),
            AchievementEntity(
                id = "super_learner",
                name = "Süper Öğrenci",
                description = "1000 puana ulaş",
                category = AchievementCategory.MASTER.name,
                icon = "🌟",
                requiredPoints = 1000,
                rewardPoints = 500
            )
        )
    }
    
    suspend fun checkAndUnlockAchievements(totalPoints: Int) {
        val lockedAchievements = achievementDao.getLockedAchievements().first()
        
        lockedAchievements.forEach { achievement ->
            if (totalPoints >= achievement.requiredPoints) {
                achievementDao.unlockAchievement(
                    achievement.id,
                    System.currentTimeMillis()
                )
                // Show notification or celebration
                showAchievementUnlocked(achievement)
            }
        }
    }
    
    private fun showAchievementUnlocked(achievement: AchievementEntity) {
        // This would show a toast or dialog when achievement is unlocked
        // Implementation can be added based on UI requirements
    }
}
