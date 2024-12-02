package com.example.sellmate.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sellmate.R
import com.example.sellmate.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,}$".toRegex()
    val isPasswordValid = passwordPattern.matches(password)

    Column(
        modifier = modifier.fillMaxSize()
            .background(Color(0xFF7F91BF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /*Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(120.dp).clip(CircleShape)
        )*/

        Spacer( modifier = Modifier.height(16.dp))
        Text(text = "SIGN IN",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // OutlinedTextField untuk Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth(0.6f),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFD4C5A0)
            )
        )

        // OutlinedTextField untuk Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            isError = !isPasswordValid,
            modifier = Modifier
                .fillMaxWidth(0.6f),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFD4C5A0)
            )
        )

        // Display error message if the password is invalid
        if (!isPasswordValid && password.isNotEmpty()) {
            Text(
                text = "*kata sandi harus paling tidak 6 karakter dan sertakan kombinasi angka, huruf dan karakter khusus (!@\$@%))",
                fontSize = 10.sp,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Tombol Sign In
        PressableButton(
            text = "Sign In",
            normalColor = Color(0xFF1A1A57),
            pressedColor = Color(0xFFD4C5A0),
            onClick = { navController.navigate("login") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Text "Don't have an account?" dan "Sign Up" yang bisa diklik terpisah
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
