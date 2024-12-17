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
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Validasi email dan password
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    val isEmailValid = email.matches(emailPattern)

    val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,}$".toRegex()
    val isPasswordValid = password.matches(passwordPattern)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF7F91BF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.sellmate),
            contentDescription = "Logo",
            modifier = Modifier
                .height(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Judul
        Text(
            text = "SIGN UP",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Nama
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                }
            },
            isError = !isPasswordValid && password.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Sign Up
        Button(
            onClick = {
                errorMessage = null
                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    errorMessage = "Semua kolom harus diisi."
                } else if (!isEmailValid) {
                    errorMessage = "Email tidak valid."
                } else if (!isPasswordValid) {
                    errorMessage = "Password tidak valid."
                } else {
                    isLoading = true
                    // Cek jika email sudah terdaftar
                    auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { fetchTask ->
                        if (fetchTask.isSuccessful) {
                            val result = fetchTask.result?.signInMethods
                            if (result != null && result.isNotEmpty()) {
                                errorMessage = "Email sudah terdaftar."
                                isLoading = false
                            } else {
                                // Daftar pengguna
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val user = auth.currentUser
                                            val userData = hashMapOf(
                                                "name" to name,
                                                "email" to email
                                            )
                                            // Simpan ke Firestore
                                            user?.let {
                                                firestore.collection("users")
                                                    .document(it.uid)
                                                    .set(userData)
                                                    .addOnSuccessListener {
                                                        auth.signOut() // Logout pengguna yang baru
                                                        navController.navigate("login") // Arahkan ke halaman login
                                                    }
                                                    .addOnFailureListener { e ->
                                                        errorMessage =
                                                            "Gagal menyimpan data pengguna: ${e.message}"
                                                        isLoading = false
                                                    }
                                            }
                                        } else {
                                            errorMessage =
                                                task.exception?.message ?: "Sign Up gagal."
                                            isLoading = false
                                        }
                                    }
                            }
                        } else {
                            errorMessage = "Gagal memeriksa email."
                            isLoading = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A57))
        ) {
            Text("Sign Up", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Loading Indicator
        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        }

        // Error Message
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Navigasi ke halaman Sign In
        Row {
            Text("Already have an account? ", color = Color.White)
            Text(
                "Sign In",
                color = Color(0xFF1A1A57),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }
    }
}
