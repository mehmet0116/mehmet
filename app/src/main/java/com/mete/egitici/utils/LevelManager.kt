package com.mete.egitici.utils

import com.mete.egitici.database.UserStatisticsDao
import com.mete.egitici.database.UserStatisticsEntity

/**
 * Manager for calculating and tracking user levels and experience points
 */
class LevelManager(
    private val userStatisticsDao: UserStatisticsDao
) {
    
    companion object {
        // XP required for each level increases exponentially
        private fun getXPForLevel(level: Int): Int {
            return 100 * level * level
        }
    }
    
    /**
     * Calculate level based on total XP
     */
    fun calculateLevel(totalXP: Int): Int {
        var level = 1
        var xpNeeded = getXPForLevel(level)
        
        while (totalXP >= xpNeeded) {
            level++
            xpNeeded += getXPForLevel(level)
        }
        
        return level
    }
    
    /**
     * Get XP needed for next level
     */
    fun getXPNeededForNextLevel(currentLevel: Int, currentXP: Int): Int {
        var totalXPNeeded = 0
        for (i in 1..currentLevel) {
            totalXPNeeded += getXPForLevel(i)
        }
        return totalXPNeeded - currentXP
    }
    
    /**
     * Get progress percentage towards next level
     */
    fun getProgressToNextLevel(currentLevel: Int, currentXP: Int): Float {
        val xpForCurrentLevel = getXPForLevel(currentLevel)
        val xpIntoCurrentLevel = currentXP - getTotalXPForLevel(currentLevel - 1)
        return (xpIntoCurrentLevel.toFloat() / xpForCurrentLevel.toFloat()) * 100f
    }
    
    private fun getTotalXPForLevel(level: Int): Int {
        var total = 0
        for (i in 1..level) {
            total += getXPForLevel(i)
        }
        return total
    }
    
    /**
     * Add XP and update level if needed
     */
    suspend fun addExperiencePoints(xp: Int, userId: String = "default_user"): LevelUpResult {
        val stats = userStatisticsDao.getUserStatisticsOnce(userId) 
            ?: UserStatisticsEntity(userId = userId)
        
        val newXP = stats.experiencePoints + xp
        val oldLevel = stats.level
        val newLevel = calculateLevel(newXP)
        val leveledUp = newLevel > oldLevel
        
        userStatisticsDao.updateLevelAndXP(newLevel, newXP, userId)
        
        return LevelUpResult(
            leveledUp = leveledUp,
            oldLevel = oldLevel,
            newLevel = newLevel,
            totalXP = newXP,
            xpGained = xp
        )
    }
    
    data class LevelUpResult(
        val leveledUp: Boolean,
        val oldLevel: Int,
        val newLevel: Int,
        val totalXP: Int,
        val xpGained: Int
    )
}
