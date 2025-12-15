package com.mete.egitici.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatisticsDao {
    
    @Query("SELECT * FROM user_statistics WHERE userId = :userId")
    fun getUserStatistics(userId: String = "default_user"): Flow<UserStatisticsEntity?>
    
    @Query("SELECT * FROM user_statistics WHERE userId = :userId")
    suspend fun getUserStatisticsOnce(userId: String = "default_user"): UserStatisticsEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistics(statistics: UserStatisticsEntity)
    
    @Update
    suspend fun updateStatistics(statistics: UserStatisticsEntity)
    
    @Query("UPDATE user_statistics SET totalPoints = totalPoints + :points WHERE userId = :userId")
    suspend fun addPoints(points: Int, userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET totalGamesPlayed = totalGamesPlayed + 1 WHERE userId = :userId")
    suspend fun incrementGamesPlayed(userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET totalGamesWon = totalGamesWon + 1 WHERE userId = :userId")
    suspend fun incrementGamesWon(userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET totalLessonsCompleted = totalLessonsCompleted + 1 WHERE userId = :userId")
    suspend fun incrementLessonsCompleted(userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET perfectScores = perfectScores + 1 WHERE userId = :userId")
    suspend fun incrementPerfectScores(userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET currentStreak = :streak WHERE userId = :userId")
    suspend fun updateCurrentStreak(streak: Int, userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET longestStreak = :streak WHERE userId = :userId")
    suspend fun updateLongestStreak(streak: Int, userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET totalTimeSpent = totalTimeSpent + :timeSpent WHERE userId = :userId")
    suspend fun addTimeSpent(timeSpent: Long, userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET lastPlayedDate = :date WHERE userId = :userId")
    suspend fun updateLastPlayedDate(date: Long, userId: String = "default_user")
    
    @Query("UPDATE user_statistics SET level = :level, experiencePoints = :xp WHERE userId = :userId")
    suspend fun updateLevelAndXP(level: Int, xp: Int, userId: String = "default_user")

    // Varsayılan kullanıcı için sil
    @Query("DELETE FROM user_statistics WHERE userId = :userId")
    suspend fun deleteAll(userId: String = "default_user")

    // Tüm satırları sil (parametresiz)
    @Query("DELETE FROM user_statistics")
    suspend fun deleteAllStatistics()
}
