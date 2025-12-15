package com.mete.egitici.models

data class Lesson(
    val id: Int,
    val category: String,
    val title: String,
    val topics: List<String>,
    val duration: Int,
    val difficulty: String
)
