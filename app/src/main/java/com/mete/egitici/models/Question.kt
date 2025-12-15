package com.mete.egitici.models

data class Question(
    val id: Int,
    val category: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val difficulty: String
)
