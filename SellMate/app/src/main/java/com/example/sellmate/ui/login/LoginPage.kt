package com.example.sellmate.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sellmate.data.model.User
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavHostController) {

    // Menggunakan data class User untuk menyimpan email dan password
    var user by remember { mutableStateOf(User()) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,}$".toRegex()
    val isPasswordValid = passwordPattern.matches(user.password)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF7F91BF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "SIGN IN",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = user.email,
            onValueChange = { user = user.copy(email = it) }, // Perbarui email di data class
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth(0.6f),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFD4C5A0)
            )
        )

        OutlinedTextField(
            value = user.password,
            onValueChange = { user = user.copy(password = it) }, // Perbarui password di data class
            label = { Text(text = "Password") },
            isError = !isPasswordValid,
            modifier = Modifier.fillMaxWidth(0.6f),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFD4C5A0)
            )
        )

        if (!isPasswordValid && user.password.isNotEmpty()) {
            Text(
                text = "*Kata sandi harus paling tidak 6 karakter dan sertakan kombinasi angka, huruf, dan karakter khusus (!@\$%))",
                fontSize = 10.sp,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        PressableButton(
            text = "Sign In",
            normalColor = Color(0xFF1A1A57),
            pressedColor = Color(0xFFD4C5A0),
            onClick = {
                if (user.email.isNotEmpty() && isPasswordValid) {
                    auth.signInWithEmailAndPassword(user.email, user.password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Login berhasil
                                Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home") // Arahkan ke halaman Home
                            } else {
                                // Login gagal
                                Toast.makeText(
                                    context,
                                    "Login gagal: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Harap isi email dan password yang valid.", Toast.LENGTH_SHORT).show()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
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
