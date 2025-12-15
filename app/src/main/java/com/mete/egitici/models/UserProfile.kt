package com.mete.egitici.models

data class UserProfile(
    val id: Int,
    val name: String,
    val age: Int,
    val avatarId: Int,
    val points: Int = 0,
    val level: Int = 1
)
