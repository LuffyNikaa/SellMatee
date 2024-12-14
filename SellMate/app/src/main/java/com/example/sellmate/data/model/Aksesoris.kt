package com.example.sellmate.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Int = 0,
    val quantity: Int = 0,
    var isNew: Boolean = false,
    val imageUrl: String = ""
)
