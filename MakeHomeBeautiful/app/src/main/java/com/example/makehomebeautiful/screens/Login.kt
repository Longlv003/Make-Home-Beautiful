package com.example.makehomebeautiful.screens

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.makehomebeautiful.R
import com.example.makehomebeautiful.api.RetrofitClient
import com.example.makehomebeautiful.models.RegisterRequest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Hello !",
                fontSize = 28.sp,
                color = Color.Gray
            )

            Text(
                text = "WELCOME BACK",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(25.dp)
            ) {

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Forgot Password",
                    modifier = Modifier.align(Alignment.End),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        handleLogin(email, password, context, navController)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(10.dp, RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Log in", color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        navController.navigate("register")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(10.dp, RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("SIGN UP", color = Color.White)
                }
            }
        }
    }
}

fun handleLogin(
    email: String,
    password: String,
    context: Context,
    navController: NavController
) {

    val auth = FirebaseAuth.getInstance()

    val emailTrim = email.trim()
    val passwordTrim = password.trim()

    if (emailTrim.isBlank() || passwordTrim.isBlank()) {
        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        return
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches()) {
        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        return
    }

    auth.signInWithEmailAndPassword(emailTrim, passwordTrim)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.getIdToken(true)
                    ?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result.token!!
                            verifyFirebaseUserLogin(token, navController)
                        }
                    }
            } else {
                Toast.makeText(
                    context,
                    task.exception?.message ?: "Login failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

fun verifyFirebaseUserLogin(token: String, navController: NavController) {
    val authHeader = "Bearer $token"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitClient.api.verifyFirebaseUserLogin(authHeader)
            if (response.isSuccessful) {
                CoroutineScope(Dispatchers.Main).launch {
                    navController.navigate("home")
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    val navController = rememberNavController()
    LoginScreen(navController)
}
