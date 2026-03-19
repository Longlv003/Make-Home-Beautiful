package com.example.makehomebeautiful.models

data class Address(
    var _id: String,
    var name: String,
    var phone: String,
    var address: String,
)

data class AddAddressRequest(
    val name: String,
    val phone: String,
    val address: String
)