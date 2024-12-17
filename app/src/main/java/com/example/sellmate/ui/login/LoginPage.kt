package com.example.sellmate.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sellmate.R
import com.example.sellmate.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") } // State untuk menyimpan pesan error

    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    val isEmailValid = emailPattern.matches(email)
    val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,}$".toRegex()
    val isPasswordValid = passwordPattern.matches(password)

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF7F91BF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.sellmate),
            contentDescription = "Logo",
            modifier = Modifier
                .height(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "SIGN IN",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Input Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White
            )
        )

        if (!isEmailValid && email.isNotEmpty()) {
            Text(
                text = "*Email tidak valid",
                fontSize = 10.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Input Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            isError = !isPasswordValid && password.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White
            )
        )

        if (!isPasswordValid && password.isNotEmpty()) {
            Text(
                text = "*Password minimal 6 karakter, huruf, angka, dan simbol",
                fontSize = 10.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Sign In
        Button(
            onClick = {
                if (isEmailValid && isPasswordValid) {
                    errorMessage = "" // Reset pesan error sebelum login
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("home") // Navigate to home on success
                            } else {
                                val exceptionMessage = task.exception?.message ?: ""
                                errorMessage = when {
                                    exceptionMessage.contains("no user record", ignoreCase = true) -> {
                                        "Email tidak terdaftar. Silakan daftar terlebih dahulu."
                                    }
                                    exceptionMessage.contains("password is invalid", ignoreCase = true) -> {
                                        "Password salah. Silakan coba lagi."
                                    }
                                    else -> {
                                        "Sign In gagal. Silakan periksa email dan password Anda."
                                    }
                                }
                            }
                        }
                } else {
                    errorMessage = "Email atau password tidak valid."
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A1A57)
            )
        ) {
            Text(text = "Sign In", color = Color.White, fontSize = 16.sp)
        }

        // Menampilkan pesan error
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // "Don't have an account? Sign Up"
        Row {
            Text(
                text = "Don't have an account? ",
                color = Color.White
            )
            Text(
                text = "Sign Up",
                color = Color(0xFF1A1A57),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("signup")
                }
            )
        }
    }
}
