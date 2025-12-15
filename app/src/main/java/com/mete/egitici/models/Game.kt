package com.mete.egitici.models

data class Game(
    val id: Int,
    val name: String,
    val category: String,
    val difficulty: String,
    val minAge: Int,
    val maxAge: Int,
    val description: String
)
