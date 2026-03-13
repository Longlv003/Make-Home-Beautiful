package com.example.makehomebeautiful.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.makehomebeautiful.compoment.BottomNavigationBar
import com.example.makehomebeautiful.compoment.HomeHeader

@Composable
fun HomeScreen(navController: NavController) {
    val navController = rememberNavController()

    Scaffold(

        topBar = { HomeHeader() },

        bottomBar = {
            BottomNavigationBar(navController)
        }

    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            composable("home") {
                HomeContent()
            }

            composable("bookmark") {
                BookmarkScreen()
            }

            composable("notification") {
                NotificationScreen()
            }

            composable("profile") {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun HomeContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home Screen")
    }
}