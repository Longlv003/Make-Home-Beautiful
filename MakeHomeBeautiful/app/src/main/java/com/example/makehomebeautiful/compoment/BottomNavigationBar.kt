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
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, null) }
        )

        NavigationBarItem(
            selected = currentRoute == "bookmark",
            onClick = { navController.navigate("bookmark") },
            icon = { Icon(Icons.Default.Bookmark, null) }
        )

        NavigationBarItem(
            selected = currentRoute == "notification",
            onClick = { navController.navigate("notification") },
            icon = { Icon(Icons.Default.Notifications, null) }
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            icon = { Icon(Icons.Default.Person, null) }
        )
    }
}