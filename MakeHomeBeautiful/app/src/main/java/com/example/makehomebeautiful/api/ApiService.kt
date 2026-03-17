package com.example.makehomebeautiful.api

import com.example.makehomebeautiful.models.Account
import com.example.makehomebeautiful.models.AddToCartRequest
import com.example.makehomebeautiful.models.CartItem
import com.example.makehomebeautiful.models.Category
import com.example.makehomebeautiful.models.Product
import com.example.makehomebeautiful.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("verifyFirebaseUser")
    suspend fun verifyFirebaseUserLogin(
        @Header("Authorization") token: String
    ): Response<DataResponse<Account>>

    @POST("verifyFirebaseUser")
    suspend fun verifyFirebaseUserRegister(
        @Header("Authorization") token: String,
        @Body body: RegisterRequest
    ): Response<DataResponse<Account>>

    @GET("category/get-list-in-stock")
    suspend fun getCategories(): Response<DataRes<List<Category>>>

    @GET("product/get-list-in-stock")
    suspend fun getProducts(): Response<DataRes<List<Product>>>

    @POST("cart/add")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body body: AddToCartRequest
    ): Response<DataRes<Any>>

    @GET("cart/get")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): Response<DataRes<List<CartItem>>>
}