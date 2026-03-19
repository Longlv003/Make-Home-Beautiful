package com.example.makehomebeautiful.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.makehomebeautiful.models.Address
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class PaymentMethod { CASH, ONLINE }
enum class ShippingMethod(val label: String, val duration: String, val price: Int) {
    EXPRESS("Giao nhanh", "1–2 ngày", 50),
    STANDARD("Giao thường", "3–5 ngày", 20)
}

class CheckoutViewModel : ViewModel() {

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

    fun loadAddresses() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: gọi API GET /account/address
            } catch (e: Exception) {
                // TODO: handle error
            } finally {
                _isLoading.value = false
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