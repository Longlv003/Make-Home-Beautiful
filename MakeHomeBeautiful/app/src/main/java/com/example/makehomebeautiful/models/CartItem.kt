package com.example.makehomebeautiful.models

data class CartItem(
    val _id: String,
    val account_id: String,
    val product_id: Product,
    val quantity: Int,
    val price: Double
)