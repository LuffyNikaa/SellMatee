package com.example.sellmate.ui.product


import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}
