package com.example.makehomebeautiful.screens

import com.example.makehomebeautiful.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun WelcomeScreen(navController: NavController) {

    LaunchedEffect(Unit) {

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            navController.navigate("home") {
                popUpTo("welcome") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Column {

                Text("MAKE YOUR", fontSize = 25.sp)

                Text(
                    "HOME BEAUTIFUL",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "The best simple place where you discover most wonderful furnitures and make your home beautiful",
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = {
                    navController.navigate("login")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text("Get Started", color = Color.White)
            }
        }
    }
}