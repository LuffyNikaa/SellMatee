package com.example.sellmate.ui.product

import HistoryViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sellmate.R
import com.example.sellmate.data.model.History
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, historyViewModel: HistoryViewModel) {
    val historyList by historyViewModel.historyList.collectAsState(initial = emptyList())
    val selectedIndex = 2 // Karena ini adalah halaman History

    // Memuat data saat layar dibuka
    LaunchedEffect(Unit) {
        historyViewModel.loadHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF7C93C3)) // Mengubah warna biru muda menjadi #7C93C3
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color = Color.White, shape = RectangleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sellmate),
                                contentDescription = "SellMate Logo",
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "SellMate History",
                            fontSize = 24.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF7C93C3), // Mengubah warna biru muda menjadi #7C93C3
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemSelected = { newIndex -> /* handle the item click */ },
                navController = navController
            )
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
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavController // Parameter ini diperlukan
) {
    NavigationBar(
        containerColor = Color(0xFF8DA7CC)
    ) {
        val selectedIconColor = Color.Black
        val unselectedIconColor = Color(0xFF4A4A4A)

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = if (selectedIndex == 0) selectedIconColor else unselectedIconColor
                )
            },
            label = { Text("Home") },
            selected = selectedIndex == 0,
            onClick = {
                onItemSelected(0)
                navController.navigate("home") // Navigasi ke halaman Home
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.ShoppingBag,
                    contentDescription = "Products",
                    tint = if (selectedIndex == 1) selectedIconColor else unselectedIconColor
                )
            },
            label = { Text("Products") },
            selected = selectedIndex == 1,
            onClick = {
                onItemSelected(1)
                navController.navigate("product") // Navigasi ke halaman Products
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.History,
                    contentDescription = "History",
                    tint = if (selectedIndex == 2) selectedIconColor else unselectedIconColor
                )
            },
            label = { Text("History") },
            selected = selectedIndex == 2,
            onClick = {
                onItemSelected(2)
                navController.navigate("history") // Navigasi ke halaman History
            }
        )
    }
}

@Composable
fun HistoryItem(history: History) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)) // Warna krem
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
