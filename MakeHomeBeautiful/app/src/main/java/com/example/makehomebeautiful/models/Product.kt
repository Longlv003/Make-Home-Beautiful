package com.example.makehomebeautiful.models

data class Product(
    val _id: String,
    val category_id: String,
    val product_name: String,
    val description: String,
    val quantity: Int,
    val price: Double,
    val image: String,
)