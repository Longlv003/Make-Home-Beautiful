package com.example.makehomebeautiful.compoment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.makehomebeautiful.models.Product
import com.example.makehomebeautiful.utils.formatPrice
import com.example.makehomebeautiful.viewmodels.AddToCartState
import com.example.makehomebeautiful.viewmodels.CartViewModel

@Composable
fun ProductItem(
    product: Product,
    cartViewModel: CartViewModel = viewModel()
) {
    var isFavorite by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val addToCartState by cartViewModel.addToCartState.collectAsState()
    val isLoading = addToCartState is AddToCartState.Loading

    LaunchedEffect(addToCartState) {
        when (addToCartState) {
            is AddToCartState.Success -> {
                showDialog = false  // ← đóng dialog ở đây
            }
            else -> {}
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.product_name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(30.dp)
                        .shadow(2.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .clickable { isFavorite = !isFavorite }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark
                        else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isFavorite) Color(0xFFFF6B35) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.product_name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Giá: ${formatPrice(product.price)}",
                        color = Color(0xFFFF6B35),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Còn: ${product.quantity}", color = Color.Gray, fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFF6B35), CircleShape)
                        .clickable { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to cart",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddToCartDialog(
            product = product,
            isLoading = isLoading,
            onDismiss = { if (!isLoading) showDialog = false },
            onConfirm = { qty ->
                cartViewModel.addToCart(
                    productId = product._id,
                    quantity = qty,
                    price = product.price
                )
            }
        )
    }
}