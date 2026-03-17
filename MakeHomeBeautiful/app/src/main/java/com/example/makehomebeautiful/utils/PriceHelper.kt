package com.example.makehomebeautiful.utils

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    return "${formatter.format(price * 1000)} đ"
}