package com.example.makehomebeautiful.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.makehomebeautiful.models.AddToCartRequest
import com.example.makehomebeautiful.api.RetrofitClient
import com.example.makehomebeautiful.models.CartItem
import com.example.makehomebeautiful.utils.TokenHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AddToCartState {
    object Idle : AddToCartState()
    object Loading : AddToCartState()
    object Success : AddToCartState()
    data class Error(val message: String) : AddToCartState()
}

class CartViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Idle)
    val addToCartState: StateFlow<AddToCartState> = _addToCartState

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addToCart(productId: String, quantity: Int, price: Double) {
        viewModelScope.launch {
            _addToCartState.value = AddToCartState.Loading
            try {
                val token = TokenHelper.getToken()
                if (token == null) {
                    _addToCartState.value = AddToCartState.Error("Vui lòng đăng nhập lại")
                    return@launch
                }

                val response = RetrofitClient.api.addToCart(
                    token = "Bearer $token",
                    body = AddToCartRequest(
                        product_id = productId,
                        quantity = quantity,
                        price = price
                    )
                )

//                Log.d("CART", "Response code: ${response.code()}")
//                Log.d("CART", "Response body: ${response.body()}")
//                Log.d("CART", "Error body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    _addToCartState.value = AddToCartState.Success
                } else {
                    _addToCartState.value = AddToCartState.Error("Thêm vào giỏ thất bại")
                }

            } catch (e: Exception) {
                _addToCartState.value = AddToCartState.Error(e.message ?: "Lỗi không xác định")
                Log.e("CART", "Exception: ${e.message}", e)
            }
        }
    }

    fun resetState() {
        _addToCartState.value = AddToCartState.Idle
    }

    fun getCart() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = TokenHelper.getToken() ?: return@launch
                val response = RetrofitClient.api.getCart("Bearer $token")
                if (response.isSuccessful) {
                    val items = response.body()?.data ?: emptyList()
//                    Log.d("CART", "Cart items: $items")
                    _cartItems.value = items
                }
            } catch (e: Exception) {
                Log.e("CART", "getCart error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}