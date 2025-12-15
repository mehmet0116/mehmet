package com.mete.egitici.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val icon: String,
    val requiredPoints: Int,
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null,
    val rewardPoints: Int = 0
)
