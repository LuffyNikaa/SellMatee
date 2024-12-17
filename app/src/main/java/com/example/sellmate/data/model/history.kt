package com.example.sellmate.data.model

data class History(
    val id: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
