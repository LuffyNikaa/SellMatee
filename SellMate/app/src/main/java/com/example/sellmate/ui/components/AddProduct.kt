package com.example.sellmate.ui.components


import HistoryViewModel
import android.widget.Toast
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
    var showToast by remember { mutableStateOf(false) }

    // Fungsi untuk menambah produk ke Firestore
    fun addProductToFirestore() {
        if (productName.isNotEmpty() && productCategory.isNotEmpty() && productPrice.isNotEmpty() && productQuantity.isNotEmpty()) {
            val product = Product(
                name = productName,
                category = productCategory,
                price = productPrice.toInt(),
                quantity = productQuantity.toInt()
            )

            productViewModel.addProduct(product) // Menambahkan produk ke Firestore

            // Tambahkan ke history di Firestore
            historyViewModel.addHistory("Produk ${product.name} ditambahkan pada ${formatTimestamp(System.currentTimeMillis())}")

            navController.navigate("product")
        }
    }
    // Form untuk menambahkan produk
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Add New Product",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF1976D2) // Biru
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input untuk Nama Produk
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nama Produk") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF1976D2), // Biru saat fokus
                unfocusedBorderColor = Color(0xFFBBDEFB) // Biru muda saat tidak fokus
            ),
            textStyle = TextStyle(color = Color.Black) // Menentukan warna teks dalam input field
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Kategori Produk
        OutlinedTextField(
            value = productCategory,
            onValueChange = { productCategory = it },
            label = { Text("Kategori") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF1976D2), // Biru saat fokus
                unfocusedBorderColor = Color(0xFFBBDEFB) // Biru muda saat tidak fokus
            ),
            textStyle = TextStyle(color = Color.Black) // Menentukan warna teks dalam input field
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Harga Produk
        OutlinedTextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Harga") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF1976D2), // Biru saat fokus
                unfocusedBorderColor = Color(0xFFBBDEFB) // Biru muda saat tidak fokus
            ),
            textStyle = TextStyle(color = Color.Black) // Menentukan warna teks dalam input field
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Kuantitas Produk
        OutlinedTextField(
            value = productQuantity,
            onValueChange = { productQuantity = it },
            label = { Text("Jumlah") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF1976D2), // Biru saat fokus
                unfocusedBorderColor = Color(0xFFBBDEFB) // Biru muda saat tidak fokus
            ),
            textStyle = TextStyle(color = Color.Black) // Menentukan warna teks dalam input field
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol Tambah Produk
        Button(
            onClick = { addProductToFirestore() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2) // Biru
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text("Tambah", color = Color.White)
        }

        // Menampilkan Toast jika produk berhasil ditambahkan
        if (showToast) {
            Toast.makeText(navController.context, "Product added successfully!", Toast.LENGTH_SHORT).show()
            showToast = false
        }
    }
}

