package com.example.makehomebeautiful.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.makehomebeautiful.compoment.AddressFormDialog
import com.example.makehomebeautiful.models.Address
import com.example.makehomebeautiful.utils.formatPrice
import com.example.makehomebeautiful.viewmodels.CartViewModel
import com.example.makehomebeautiful.viewmodels.CheckoutViewModel
import com.example.makehomebeautiful.viewmodels.PaymentMethod
import com.example.makehomebeautiful.viewmodels.ShippingMethod

@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val isLoading by checkoutViewModel.isLoading.collectAsState()
    val addresses by checkoutViewModel.addresses.collectAsState()
    val selectedAddress by checkoutViewModel.selectedAddress.collectAsState()
    val selectedPayment by checkoutViewModel.selectedPayment.collectAsState()
    val selectedShipping by checkoutViewModel.selectedShipping.collectAsState()
    val isPlacingOrder by checkoutViewModel.isPlacingOrder.collectAsState()
    val orderSuccess by checkoutViewModel.orderSuccess.collectAsState()

    val subtotal = cartItems.sumOf { it.price * it.quantity }

    var showAddressDialog by remember { mutableStateOf(false) }
    var showAddressFormDialog by remember { mutableStateOf(false) }
    var addressToEdit by remember { mutableStateOf<Address?>(null) }
    val isUpdatingAddress by checkoutViewModel.isUpdatingAddress.collectAsState()

    LaunchedEffect(Unit) {
        checkoutViewModel.loadAddresses()
    }

    LaunchedEffect(orderSuccess) {
        if (orderSuccess) {
            // TODO: navigate sang màn hình order success
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Check out",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(48.dp))
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // ── Shipping Address ──────────────────────────────────────
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Địa chỉ giao hàng",
                                        fontSize = 13.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (selectedAddress != null) {
                                        IconButton(
                                            onClick = { showAddressDialog = true },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Chọn địa chỉ khác",
                                                modifier = Modifier.size(16.dp),
                                                tint = Color.Gray
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(10.dp))

                                if (selectedAddress == null) {
                                    OutlinedButton(
                                        onClick = {
                                            addressToEdit = null
                                            showAddressFormDialog = true
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, Color(0xFF1D9E75)),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Color(
                                                0xFF1D9E75
                                            )
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text("Thêm địa chỉ giao hàng", fontSize = 14.sp)
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFF8F8F8),
                                        border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    selectedAddress!!.name,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 14.sp
                                                )
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    selectedAddress!!.phone,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    selectedAddress!!.address,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    lineHeight = 18.sp
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    addressToEdit = selectedAddress
                                                    showAddressFormDialog = true
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Sửa địa chỉ",
                                                    modifier = Modifier.size(14.dp),
                                                    tint = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ── Payment Method ────────────────────────────────────────
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    "Phương thức thanh toán",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(Modifier.height(10.dp))

                                listOf(
                                    PaymentMethod.CASH to Pair(
                                        "Tiền mặt khi nhận hàng",
                                        "Thanh toán khi nhận"
                                    ),
                                    PaymentMethod.ONLINE to Pair(
                                        "Chuyển khoản online",
                                        "Ngân hàng / Ví điện tử"
                                    )
                                ).forEach { (method, info) ->
                                    val isSelected = selectedPayment == method
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { checkoutViewModel.selectPayment(method) },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(
                                            if (isSelected) 1.5.dp else 0.5.dp,
                                            if (isSelected) Color(0xFF1D9E75) else Color(0xFFE0E0E0)
                                        ),
                                        color = Color.White
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { checkoutViewModel.selectPayment(method) },
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = Color(
                                                        0xFF1D9E75
                                                    )
                                                ),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    info.first,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    info.second,
                                                    fontSize = 11.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                    }

                    // ── Order Summary ─────────────────────────────────────────
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    "Tổng quan đơn hàng",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(Modifier.height(10.dp))

                                // Danh sách sản phẩm từ cart
                                cartItems.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "${item.product_id.product_name} x${item.quantity}",
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                        Text(
                                            formatPrice(item.price * item.quantity),
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Divider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    color = Color(0xFFEEEEEE)
                                )

                                // Shipping method
                                Text(
                                    "Phương thức giao hàng",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(Modifier.height(8.dp))

                                ShippingMethod.entries.forEach { method ->
                                    val isSelected = selectedShipping == method
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { checkoutViewModel.selectShipping(method) },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(
                                            if (isSelected) 1.5.dp else 0.5.dp,
                                            if (isSelected) Color(0xFF1D9E75) else Color(0xFFE0E0E0)
                                        ),
                                        color = Color.White
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = {
                                                        checkoutViewModel.selectShipping(
                                                            method
                                                        )
                                                    },
                                                    colors = RadioButtonDefaults.colors(
                                                        selectedColor = Color(0xFF1D9E75)
                                                    ),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Column {
                                                    Text(
                                                        method.label,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Text(
                                                        method.duration,
                                                        fontSize = 11.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                            }
                                            Text(
                                                formatPrice(method.price.toDouble()),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(6.dp))
                                }

                                Divider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    color = Color(0xFFEEEEEE)
                                )

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Tạm tính", fontSize = 13.sp, color = Color.Gray)
                                    Text(
                                        formatPrice(subtotal),
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Phí vận chuyển", fontSize = 13.sp, color = Color.Gray)
                                    Text(
                                        formatPrice(selectedShipping.price.toDouble()),
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Tổng cộng",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        formatPrice(subtotal + selectedShipping.price),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1D9E75)
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }

        // ── Place Order Button ────────────────────────────────────────────────
        Button(
            onClick = { checkoutViewModel.placeOrder() },
            enabled = !isPlacingOrder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2A))
        ) {
            if (isPlacingOrder) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "Đặt hàng",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }

    // ── Address Picker Dialog ─────────────────────────────────────────────────
    if (showAddressDialog) {
        Dialog(onDismissRequest = { showAddressDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Chọn địa chỉ giao hàng", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(16.dp))

                    addresses.forEach { address ->
                        val isSelected = selectedAddress?._id == address._id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    checkoutViewModel.selectAddress(address)
                                    showAddressDialog = false
                                },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                if (isSelected) 1.5.dp else 0.5.dp,
                                if (isSelected) Color(0xFF1D9E75) else Color(0xFFE0E0E0)
                            ),
                            color = Color.White
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        checkoutViewModel.selectAddress(address)
                                        showAddressDialog = false
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(
                                            0xFF1D9E75
                                        )
                                    ),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(
                                        address.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(address.phone, fontSize = 12.sp, color = Color.Gray)
                                    Text(
                                        address.address,
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            showAddressDialog = false
                            addressToEdit = null
                            showAddressFormDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFF1D9E75)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1D9E75))
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Thêm địa chỉ mới")
                    }
                }
            }
        }
    }

    if (showAddressFormDialog) {
        AddressFormDialog(
            initialAddress = addressToEdit,
            isLoading = isUpdatingAddress,  // ← THÊM: truyền vào dialog để hiện loading
            onConfirm = { name, phone, address ->
                if (addressToEdit == null) {
                    checkoutViewModel.addAddress(name, phone, address)
                    showAddressFormDialog = false  // add thì đóng ngay như cũ
                } else {
                    checkoutViewModel.updateAddress(
                        id = addressToEdit!!._id,
                        name = name,
                        phone = phone,
                        address = address,
                        onDone = { showAddressFormDialog = false }  // ← đóng sau khi xong
                    )
                    // KHÔNG đóng dialog ở đây nữa
                }
            },
            onDismiss = { showAddressFormDialog = false }
        )
    }
}