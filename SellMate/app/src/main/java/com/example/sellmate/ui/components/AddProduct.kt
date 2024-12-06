package com.example.sellmate.ui.components


import HistoryViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sellmate.data.model.Product
import com.example.sellmate.ui.product.formatTimestamp


import com.example.sellmate.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    historyViewModel: HistoryViewModel // Menambahkan HistoryViewModel
) {
    var productName by remember { mutableStateOf("") }
    var productCategory by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) } // Untuk dropdown menu
    val categories = listOf("Ring", "Bracelet", "Necklace") // Daftar kategori
    var selectedCategory by remember { mutableStateOf("") }

    // Fungsi untuk menambah produk ke Firestore
    fun addProductToFirestore() {
        if (productName.isNotEmpty() && selectedCategory.isNotEmpty() && productPrice.isNotEmpty() && productQuantity.isNotEmpty()) {
            val product = Product(
                name = productName,
                category = selectedCategory,
                price = productPrice.toInt(),
                quantity = productQuantity.toInt()
            )
            productViewModel.addProduct(product)
            historyViewModel.addHistory("Produk ${product.name} ditambahkan pada ${formatTimestamp(System.currentTimeMillis())}")
            navController.navigate("product")
        }
    }

    // Tampilan UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7C93C3)) // Warna biru muda
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Kategori (Dropdown)
        Text("Kategori", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Pilih Kategori") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color(0xFF1976D2),
                    unfocusedBorderColor = Color.Gray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Nama
        Text("Nama", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            placeholder = { Text("Nama Produk") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = Color(0xFF1976D2),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Harga
        Text("Harga", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        OutlinedTextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            placeholder = { Text("Rp") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = Color(0xFF1976D2),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Kuantitas
        Text("Jumlah", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        OutlinedTextField(
            value = productQuantity,
            onValueChange = { productQuantity = it },
            placeholder = { Text("Jumlah") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = Color(0xFF1976D2),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol Tambah
        Button(
            onClick = { addProductToFirestore() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF001F54) // Warna biru gelap
            )
        ) {
            Text("Add", color = Color.White, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
