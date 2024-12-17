package com.example.sellmate.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sellmate.Home
import com.example.sellmate.R
import com.example.sellmate.ui.theme.SellMateTheme

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF7F91BF)),  // Warna latar belakang sesuai desain
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /*Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.
            height(120.dp).
            width(120.dp).
            clip(CircleShape)
        )*/

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Sell Mate",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF000000)  // Warna teks gelap
        )

        Spacer(modifier = Modifier.height(48.dp))
        // Tombol Sign In
        PressableButton(
            text = "Sign In",
            normalColor = Color(0xFF1A1A57),
            pressedColor = Color(0xFFD4C5A0),
            onClick = { navController.navigate("login") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Sign Up
        PressableButton(
            text = "Sign Up",
            normalColor = Color(0xFF1A1A57),
            pressedColor = Color(0xFFD4C5A0),
            onClick = { navController.navigate("signup") }
        )
    }
}

@Composable
fun PressableButton(
    text: String,
    normalColor: Color,
    pressedColor: Color,
    onClick: () -> Unit
) {
    // Interaction source untuk mendeteksi apakah tombol sedang ditekan
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    // Mengubah warna tombol sesuai kondisi `isPressed`
    val backgroundColor = if (isPressed) pressedColor else normalColor
    val contentColor = if (isPressed) Color.Black else Color.White

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        interactionSource = interactionSource,
        modifier = Modifier.width(200.dp)
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    SellMateTheme {
        LandingScreen(navController = rememberNavController()) // Menampilkan preview dengan navController
    }
}
