package com.example.makehomebeautiful.api

data class DataResponse<T>(
    val dataRes: DataRes<T>
)

data class DataRes<T>(
    val msg: String,
    val data: T?
)