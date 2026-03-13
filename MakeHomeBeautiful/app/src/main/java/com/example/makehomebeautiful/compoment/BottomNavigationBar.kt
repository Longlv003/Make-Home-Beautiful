package com.example.makehomebeautiful.compoment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("home")
            },
            icon = { Icon(Icons.Default.Home, null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("bookmark")
            },
            icon = { Icon(Icons.Default.Bookmark, null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("notification")
            },
            icon = { Icon(Icons.Default.Notifications, null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("profile")
            },
            icon = { Icon(Icons.Default.Person, null) }
        )
    }
}