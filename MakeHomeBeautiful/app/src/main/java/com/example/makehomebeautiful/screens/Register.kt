package com.example.makehomebeautiful.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.makehomebeautiful.R
import com.example.makehomebeautiful.api.RetrofitClient
import com.example.makehomebeautiful.models.RegisterRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                text = "WELCOME",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "REGISTER !",
                fontSize = 28.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(25.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

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

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        if (!isLoading) {
                            isLoading = true
                            handleRegister(
                                name,
                                email,
                                password,
                                confirmPassword,
                                auth,
                                context,
                                navController
                            ) {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(10.dp, RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("SIGN UP", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                ClickableText(
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    ),
                    text = buildAnnotatedString {

                        append("Already have account? ")

                        pushStringAnnotation(
                            tag = "SIGN_IN",
                            annotation = "login"
                        )

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("SIGN IN")
                        }

                        pop()
                    },
                    onClick = {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}

fun handleRegister(
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    auth: FirebaseAuth,
    context: android.content.Context,
    navController: NavController,
    onFinish: () -> Unit
) {

    if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        onFinish()
        return
    }

    if (password.trim() != confirmPassword.trim()) {
        Toast.makeText(context, "Password not match", Toast.LENGTH_SHORT).show()
        onFinish()
        return
    }

    auth.createUserWithEmailAndPassword(email.trim(), password.trim())
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser

                val profileUpdates = userProfileChangeRequest {
                    displayName = name.trim()
                }

                user?.updateProfile(profileUpdates)

                user?.getIdToken(true)
                    ?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result.token!!
                            verifyFirebaseUser(token, name.trim(), navController)
                        }
                    }
            } else {
                Toast.makeText(
                    context,
                    task.exception?.message ?: "Register failed",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(
                    "REGISTER_ERROR",
                    "Register failed: ${task.exception?.message}",
                    task.exception
                )
                onFinish()
            }
        }
}

fun verifyFirebaseUser(token: String, name: String, navController: NavController) {
    val authHeader = "Bearer $token"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitClient.api.verifyFirebaseUserRegister(authHeader, RegisterRequest(name))
            if (response.isSuccessful) {
                CoroutineScope(Dispatchers.Main).launch {
                    navController.navigate("login")
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

@Preview
@Composable
fun RegisterPreview() {
    val navController = rememberNavController()
    RegisterScreen(navController)
}