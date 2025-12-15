package com.mete.egitici.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val rewardPoints: Int,
    val date: Long,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null,
    val taskType: String,
    val targetValue: Int = 1,
    val currentProgress: Int = 0
)
