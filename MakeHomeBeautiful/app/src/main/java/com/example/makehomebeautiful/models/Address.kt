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

data class AddressResponse(
    val success: Boolean,
    val message: String? = null,
    val data: Address? = null
)

data class AddressListResponse(
    val success: Boolean,
    val data: List<Address>
)