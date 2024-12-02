package com.example.sellmate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.sellmate.ui.login.LandingScreen
import com.example.sellmate.ui.login.LoginPage
import com.example.sellmate.ui.login.SignupPage
import com.example.sellmate.viewmodel.AuthViewModel

@Composable
fun MyAppNavHost(modifier: Modifier = Modifier, authViewModel: AuthViewModel){
    val navController = rememberNavController()

    NavHost(navController, startDestination= "home", builder = {

        composable("home") {
            LandingScreen(modifier, navController)
        }

        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }

        composable("signup") {
            SignupPage(modifier,navController)
        }

    })
}