package com.example.sellmate.ui.product

import HistoryViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sellmate.R
import com.example.sellmate.data.model.History
import com.example.sellmate.ui.product.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, historyViewModel: HistoryViewModel) {
    val historyList by historyViewModel.historyList.collectAsState(initial = emptyList())

    // Memuat data saat layar dibuka
    LaunchedEffect(Unit) {
        historyViewModel.loadHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.sellmate), // Logo SellMate
                            contentDescription = "SellMate Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f)) // Menambahkan ruang agar tulisan berada di tengah
                        Text("HISTORY", fontSize = 20.sp, color = Color.White)
                        Spacer(modifier = Modifier.weight(1f)) // Menambahkan ruang agar tulisan berada di tengah
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2), // Warna latar biru
                    titleContentColor = Color.White // Warna teks putih
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(historyList) { history ->
                    HistoryItem(history = history)
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar(
        containerColor = Color(0xFF1976D2),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationItem(
                    icon = Icons.Filled.Home,
                    label = "Home",
                    onClick = { navController.navigate("home") }
                )
                NavigationItem(
                    icon = Icons.Filled.ShoppingBag,
                    label = "Products",
                    onClick = { navController.navigate("product") }
                )
                NavigationItem(
                    icon = Icons.Filled.History,
                    label = "History",
                    onClick = { /* Stay on History */ }
                )
            }
        }
    )
}

@Composable
fun NavigationItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(label, fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun HistoryItem(history: History) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)) // Warna biru muda
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Deskripsi: ${history.description}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Waktu: ${formatTimestamp(history.timestamp)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF1976D2)
            )
        }
    }
}
