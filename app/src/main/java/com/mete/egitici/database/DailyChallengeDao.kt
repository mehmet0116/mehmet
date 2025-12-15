package com.mete.egitici.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyChallengeDao {
    
    @Query("SELECT * FROM daily_challenges WHERE date >= :startDate ORDER BY date DESC")
    fun getActiveChallenges(startDate: Long): Flow<List<DailyChallengeEntity>>
    
    @Query("SELECT * FROM daily_challenges WHERE date = :date")
    fun getChallengesForDate(date: Long): Flow<List<DailyChallengeEntity>>
    
    @Query("SELECT * FROM daily_challenges WHERE id = :challengeId")
    suspend fun getChallengeById(challengeId: String): DailyChallengeEntity?
    
    @Query("SELECT * FROM daily_challenges WHERE isCompleted = 0 AND date <= :currentDate ORDER BY date DESC LIMIT 1")
    suspend fun getTodayChallenge(currentDate: Long): DailyChallengeEntity?
    
    @Query("SELECT * FROM daily_challenges WHERE isCompleted = 1 ORDER BY completedDate DESC")
    fun getCompletedChallenges(): Flow<List<DailyChallengeEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: DailyChallengeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(challenges: List<DailyChallengeEntity>)
    
    @Update
    suspend fun updateChallenge(challenge: DailyChallengeEntity)
    
    @Query("UPDATE daily_challenges SET isCompleted = 1, completedDate = :completedDate WHERE id = :challengeId")
    suspend fun completeChallenge(challengeId: String, completedDate: Long)
    
    @Query("UPDATE daily_challenges SET currentProgress = :progress WHERE id = :challengeId")
    suspend fun updateProgress(challengeId: String, progress: Int)
    
    @Query("SELECT COUNT(*) FROM daily_challenges WHERE isCompleted = 1")
    suspend fun getCompletedCount(): Int
    
    @Query("DELETE FROM daily_challenges WHERE date < :oldDate")
    suspend fun deleteOldChallenges(oldDate: Long)
}
