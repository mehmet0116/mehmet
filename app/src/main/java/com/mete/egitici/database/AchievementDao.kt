package com.mete.egitici.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    
    @Query("SELECT * FROM achievements ORDER BY category, requiredPoints")
    fun getAllAchievements(): Flow<List<AchievementEntity>>
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 1 ORDER BY unlockedDate DESC")
    fun getUnlockedAchievements(): Flow<List<AchievementEntity>>
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 0 ORDER BY requiredPoints")
    fun getLockedAchievements(): Flow<List<AchievementEntity>>
    
    @Query("SELECT * FROM achievements WHERE id = :achievementId")
    suspend fun getAchievementById(achievementId: String): AchievementEntity?
    
    @Query("SELECT * FROM achievements WHERE category = :category")
    fun getAchievementsByCategory(category: String): Flow<List<AchievementEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: AchievementEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)
    
    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)
    
    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :unlockedDate WHERE id = :achievementId")
    suspend fun unlockAchievement(achievementId: String, unlockedDate: Long)
    
    @Query("SELECT COUNT(*) FROM achievements WHERE isUnlocked = 1")
    suspend fun getUnlockedCount(): Int
    
    @Delete
    suspend fun deleteAchievement(achievement: AchievementEntity)
}
