package com.example.sellmate

import HistoryViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sellmate.Profile.ProfileScreen
import com.example.sellmate.ui.components.AddProductScreen
import com.example.sellmate.ui.login.LandingScreen
import com.example.sellmate.ui.login.LoginPage
import com.example.sellmate.ui.login.SignupPage
import com.example.sellmate.ui.product.HistoryScreen
import com.example.sellmate.ui.product.ProductScreen
import com.example.sellmate.ui.theme.SellMateTheme
import com.example.sellmate.viewmodel.AuthState
import com.example.sellmate.viewmodel.AuthViewModel
import com.example.sellmate.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SellMateTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val productViewModel: ProductViewModel = viewModel()
                val historyViewModel: HistoryViewModel = viewModel()

                // Inisialisasi SharedPreferences di AuthViewModel
                LaunchedEffect(Unit) {
                    authViewModel.initialize(this@MainActivity)
                }

                // Observe status autentikasi
                val authState = authViewModel.authState.observeAsState()

                // Navigasi berdasarkan status autentikasi
                LaunchedEffect(authState.value) {
                    when (authState.value) {
                        is AuthState.Authenticated -> {
                            // Navigate directly to home if authenticated
                            navController.navigate("home") {
                                // Pop up to "landing" if previously navigated
                                popUpTo("landing") { inclusive = true }
                            }
                        }
                        is AuthState.Unauthenticated -> {
                            // If unauthenticated, navigate to landing page
                            navController.navigate("landing") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                        else -> Unit // Wait for login state to be determined
                    }
                }

                // Setup navigasi
                NavHost(navController = navController, startDestination = "landing") {

                    composable("landing") {
                        LandingScreen(
                            modifier = Modifier,
                            authViewModel = authViewModel,
                            navController = navController)
                    }
                    composable("signup") {
                        SignupPage(
                            modifier = Modifier,
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }
                    composable("login") {
                        LoginPage(
                            modifier = Modifier,
                            navController = navController,
                            authViewModel = authViewModel
                        )
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
                        HistoryScreen(
                            navController = navController,
                            historyViewModel = historyViewModel
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }
                }
            }
        }
    }
}
