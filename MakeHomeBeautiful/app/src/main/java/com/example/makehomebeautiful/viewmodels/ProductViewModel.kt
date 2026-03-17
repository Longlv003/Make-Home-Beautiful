package com.example.makehomebeautiful.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.makehomebeautiful.api.RetrofitClient
import com.example.makehomebeautiful.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchProduct()
    }

    private fun fetchProduct() {
        viewModelScope.launch() {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    _products.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}