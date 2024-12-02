package com.example.sellmate


import HistoryViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sellmate.ui.components.AddProductScreen
import com.example.sellmate.ui.product.HistoryScreen
import com.example.sellmate.ui.product.ProductScreen
import com.example.sellmate.ui.theme.SellMateTheme

import com.example.sellmate.viewmodel.ProductViewModel
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SellMateTheme {
                val navController = rememberNavController()

                // Mengambil instance ViewModel
                val productViewModel: ProductViewModel = viewModel()
                val historyViewModel: HistoryViewModel = viewModel() // Tambahkan ViewModel History

                NavHost(navController = navController, startDestination = "home") {
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
                            historyViewModel = historyViewModel // Pass History ViewModel
                        )
                    }
                    composable("history") {
                        HistoryScreen( navController = navController,historyViewModel = historyViewModel) // Halaman Riwayat
                    }

                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}
