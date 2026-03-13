package com.example.makehomebeautiful

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.makehomebeautiful.screens.HomeScreen
import com.example.makehomebeautiful.screens.LoginScreen
import com.example.makehomebeautiful.screens.RegisterScreen
import com.example.makehomebeautiful.screens.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "welcome"
            ) {
                composable("welcome") {
                    WelcomeScreen(navController)
                }
                composable("login") {
                    LoginScreen(navController)
                }
                composable("register") {
                    RegisterScreen(navController)
                }
                composable("home") {
                    HomeScreen()
                }
            }
        }
    }
}