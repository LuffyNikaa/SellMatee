@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.sellmate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sellmate.ui.theme.SellMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Logo bundar di kiri atas
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color = Color.White, shape = CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sellmate),
                                // Ganti dengan logo Anda
                                contentDescription = "SellMate Logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp)) // Spacer untuk memberi jarak antara logo dan teks

                        // Teks "SellMate" di samping logo
                        Text(
                            text = "SellMate",
                            style = MaterialTheme.typography.titleLarge, // Menggunakan style titleLarge
                            color = Color.White // Warna teks putih
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF7C93C3) // Latar belakang TopBar
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile Icon", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addProduct") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Product")
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex.value,
                onItemSelected = { selectedIndex.value = it },
                navController = navController
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedIndex.value) {
                0 -> HomeScreen(navController) // Home Screen
                1 -> ProductScreen(navController) // Product Screen
                2 -> HistoryScreen(navController) // History Screen
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavController
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
                navController.navigate("home")
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
                navController.navigate("product")
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
                navController.navigate("history")
            }
        )
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Gambar orang melambai
        Image(
            painter = painterResource(id = R.drawable.orang), // Ganti dengan resource gambar
            contentDescription = "Waving Person",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Memberi jarak setelah gambar

        // Teks "Selamat Datang"
        Text(
            text = "Selamat Datang di aplikasi SellMate",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp)) // Memberi jarak sebelum box

        // Box dengan penjelasan aplikasi SellMate
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color(0xFF7C93C3)) // Warna latar belakang box
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Apa itu SellMate?",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp)) // Memberi jarak setelah judul

                Text(
                    text = "SellMate adalah aplikasi!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}


@Composable
fun ProductScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Product Screen")
    }
}

@Composable
fun HistoryScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("History Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SellMateTheme {
        Home(navController = rememberNavController())
    }
}
