@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.sellmate

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.sellmate.ui.theme.SellMateTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(0) }

    // Set up NavHost for navigation management


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SearchBar(navController) },
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
fun SearchBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = "",
            onValueChange = { /* Handle search text change */ },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Search") },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFEADAB7),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        IconButton(onClick = {
            navController.navigate("profile") // Navigasi ke halaman Profil
        }) {
            Icon(Icons.Filled.Person, contentDescription = "Profile Icon")
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home Screen")
        Button(onClick = {
            navController.navigate("product") // Navigasi ke Product Screen
        }) {
            Text("Go to Products")
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
        Button(onClick = {
            navController.navigate("history") // Navigasi ke History Screen
        }) {
            Text("Go to History")
        }
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
        Button(onClick = {
            navController.navigate("product") // Navigasi kembali ke Product Screen
        }) {
            Text("Back to Products")
        }
    }
}

@Composable
fun AddProductScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Add Product Screen")
        Button(onClick = {
            navController.popBackStack() // Kembali ke halaman sebelumnya
        }) {
            Text("Back to Home")
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile Screen")
        Button(onClick = {
            navController.popBackStack() // Kembali ke halaman sebelumnya
        }) {
            Text("Back to Home")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SellMateTheme {
        Home(navController = rememberNavController()) // Menampilkan preview dengan navController
    }
}
