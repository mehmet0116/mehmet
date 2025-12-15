package com.mete.egitici.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val gameId: Int,
    val score: Int,
    val completedAt: Long,
    val stars: Int
)
