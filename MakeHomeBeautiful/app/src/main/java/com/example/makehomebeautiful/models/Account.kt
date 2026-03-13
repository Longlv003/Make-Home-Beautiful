package com.example.makehomebeautiful.models

import java.util.Date

data class Account(
    val _id: String,
    val firebase_uid: String,
    val name: String,
    val email: String,
    val role: String,
    val image: String,
//    val is_active: Boolean,
//    val deleted_at: Date?,
)

