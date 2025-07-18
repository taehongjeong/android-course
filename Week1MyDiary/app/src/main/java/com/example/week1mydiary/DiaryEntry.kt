package com.example.week1mydiary

import java.util.Date

data class DiaryEntry(
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: Date = Date(),
    val mood: String = "😊"
)