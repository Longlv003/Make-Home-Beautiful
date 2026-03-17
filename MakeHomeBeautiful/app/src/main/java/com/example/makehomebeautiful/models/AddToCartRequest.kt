package com.example.makehomebeautiful.models

data class AddToCartRequest(
    val product_id: String,
    val quantity: Int,
    val price: Double
)