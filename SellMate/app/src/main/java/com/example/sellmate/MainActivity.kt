package com.example.sellmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sellmate.ui.components.AddProductScreen
import com.example.sellmate.ui.login.LandingScreen
import com.example.sellmate.ui.login.LoginPage
import com.example.sellmate.ui.login.SignupPage
import com.example.sellmate.ui.product.HistoryScreen
import com.example.sellmate.ui.product.ProductScreen
import com.example.sellmate.ui.theme.SellMateTheme
import androidx.activity.viewModels
import com.example.sellmate.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SellMateTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "landing") {
                    composable("landing") {
                        LandingScreen(navController = navController)
                    }

                    composable("login") {
                        LoginPage(navController = navController)
                    }

                    composable("signup") {
                        SignupPage(navController = navController)
                    }

                    composable("home") {
                        Home(navController = navController)
                    }
                    composable("addProduct") {
                        AddProductScreen(
                            navController = navController,
                            productViewModel = productViewModel,
                            historyViewModel = historyViewModel
                        )
                    }
                    composable("product") {
                        ProductScreen(
                            navController = navController,
                            productViewModel = productViewModel,
                            historyViewModel = historyViewModel
                        )
                    }
                    composable("history") {
                        HistoryScreen(historyViewModel = historyViewModel)
                    }

                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}
