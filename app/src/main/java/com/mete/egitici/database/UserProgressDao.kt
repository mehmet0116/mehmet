package com.mete.egitici.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserProgressDao {
    
    @Insert
    suspend fun insert(userProgress: UserProgressEntity)
    
    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    suspend fun getUserProgress(userId: Int): List<UserProgressEntity>
    
    @Query("SELECT * FROM user_progress WHERE userId = :userId AND gameId = :gameId")
    suspend fun getGameProgress(userId: Int, gameId: Int): List<UserProgressEntity>
}
