package com.example.makehomebeautiful.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.makehomebeautiful.compoment.BottomNavigationBar
import com.example.makehomebeautiful.compoment.CategoryItem
import com.example.makehomebeautiful.compoment.HomeHeader
import com.example.makehomebeautiful.compoment.ProductItem
import com.example.makehomebeautiful.viewmodels.AddToCartState
import com.example.makehomebeautiful.viewmodels.CartViewModel
import com.example.makehomebeautiful.viewmodels.CategoryViewModel
import com.example.makehomebeautiful.viewmodels.ProductViewModel

@Composable
fun HomeScreen() {

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            HomeHeader(currentRoute, navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeContent() }
            composable("cart") { CartScreen(navController) }
            composable("bookmark") { BookmarkScreen() }
            composable("notification") { NotificationScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}

@Composable
fun HomeContent(
    categoryViewModel: CategoryViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {

    val categories by categoryViewModel.categories.collectAsState()
    val isLoadingCategory by categoryViewModel.isLoading.collectAsState()

    val products by productViewModel.products.collectAsState()
    val isLoadingProduct by productViewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val addToCartState by cartViewModel.addToCartState.collectAsState()

    LaunchedEffect(addToCartState) {
        when (addToCartState) {
            is AddToCartState.Success -> {
                Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                cartViewModel.resetState()
            }

            is AddToCartState.Error -> {
                Toast.makeText(
                    context,
                    (addToCartState as AddToCartState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                cartViewModel.resetState()
            }

            else -> {}
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item(span = { GridItemSpan(2) }) {
            if (isLoadingCategory) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(category = category)
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Products",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        if (isLoadingProduct) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(products) { product ->
                ProductItem(
                    product = product,
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}