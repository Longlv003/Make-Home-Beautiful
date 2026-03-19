package com.example.makehomebeautiful.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.makehomebeautiful.api.RetrofitClient
import com.example.makehomebeautiful.models.AddAddressRequest
import com.example.makehomebeautiful.models.Address
import com.example.makehomebeautiful.utils.TokenHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class PaymentMethod { CASH, ONLINE }
enum class ShippingMethod(val label: String, val duration: String, val price: Int) {
    EXPRESS("Giao nhanh", "1–2 ngày", 50),
    STANDARD("Giao thường", "3–5 ngày", 20)
}

class CheckoutViewModel : ViewModel() {
    private val _isUpdatingAddress = MutableStateFlow(false)
    val isUpdatingAddress: StateFlow<Boolean> = _isUpdatingAddress
    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedPayment = MutableStateFlow(PaymentMethod.CASH)
    val selectedPayment: StateFlow<PaymentMethod> = _selectedPayment

    private val _selectedShipping = MutableStateFlow(ShippingMethod.EXPRESS)
    val selectedShipping: StateFlow<ShippingMethod> = _selectedShipping

    private val _isPlacingOrder = MutableStateFlow(false)
    val isPlacingOrder: StateFlow<Boolean> = _isPlacingOrder

    private val _orderSuccess = MutableStateFlow(false)
    val orderSuccess: StateFlow<Boolean> = _orderSuccess

    fun loadAddresses(keepSelectedId: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = TokenHelper.getToken()
                if (token == null) {
                    _isLoading.value = false
                    return@launch
                }

                val response = RetrofitClient.api.getAddresses("Bearer $token")
                if (response.isSuccessful) {
                    val list = response.body()?.data ?: emptyList()
                    _addresses.value = list
                    _selectedAddress.value = if (keepSelectedId != null) {
                        list.find { it._id == keepSelectedId } ?: list.firstOrNull()
                    } else {
                        _selectedAddress.value?.let { current ->
                            list.find { it._id == current._id }
                        } ?: list.firstOrNull()
                    }
                }
            } catch (e: Exception) {
                // TODO: handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addAddress(name: String, phone: String, address: String) {
        viewModelScope.launch {
            try {
                val token = TokenHelper.getToken()
                if (token == null) return@launch

                val response = RetrofitClient.api.addAddress(
                    token = "Bearer $token",
                    request = AddAddressRequest(name, phone, address)
                )
                if (response.isSuccessful) {
                    loadAddresses()
                }
            } catch (e: Exception) {
                // TODO: handle error
            }
        }
    }

    fun updateAddress(id: String, name: String, phone: String, address: String, onDone: () -> Unit) {
        viewModelScope.launch {
            _isUpdatingAddress.value = true
            try {
                val token = TokenHelper.getToken()
                if (token == null) return@launch

                val response = RetrofitClient.api.updateAddress(
                    token = "Bearer $token",
                    id = id,
                    request = AddAddressRequest(name, phone, address)
                )
                if (response.isSuccessful) {
                    loadAddresses(keepSelectedId = _selectedAddress.value?._id)  // ← CHANGED: giữ selected
                    withContext(Dispatchers.Main) { onDone() }// ← đóng dialog SAU KHI api xong
                }
            } catch (e: Exception) {
                // TODO: handle error
            } finally {
                _isUpdatingAddress.value = false
            }
        }
    }

    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }

    fun selectPayment(method: PaymentMethod) {
        _selectedPayment.value = method
    }

    fun selectShipping(method: ShippingMethod) {
        _selectedShipping.value = method
    }

    fun placeOrder() {
        viewModelScope.launch {
            _isPlacingOrder.value = true
            try {
                // TODO: gọi API POST /order
            } catch (e: Exception) {
                // TODO: handle error
            } finally {
                _isPlacingOrder.value = false
            }
        }
    }
}