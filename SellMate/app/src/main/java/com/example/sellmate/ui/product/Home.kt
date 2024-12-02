package com.example.sellmate.ui.product

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun ProdukScreen() {
    val productList = remember { mutableStateOf<List<product>>(emptyList()) }

    // Ambil data produk dari Firestore
    LaunchedEffect(true) {
        getProductsFromFirestore(productList)
    }

    // Menampilkan daftar produk
    LazyColumn {
        items(productList.value) { product ->
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nama: ${product.name}")
            Text(text = "Tipe: ${product.type}")
            Text(text = "Harga: ${product.price}")
            Text(text = "Jumlah: ${product.quantity}")
        }
    }
}

fun getProductsFromFirestore(productList: MutableState<List<Product>>) {
    db.collection("products").get().addOnSuccessListener { snapshot ->
        val products = snapshot.documents.mapNotNull { document ->
            val name = document.getString("name") ?: ""
            val type = document.getString("type") ?: ""
            val price = document.getDouble("price") ?: 0.0
            val quantity = document.getLong("quantity")?.toInt() ?: 0
            if (name.isNotEmpty() && type.isNotEmpty()) {
                Product(name, type, price, quantity)
            } else {
                null
            }
        }
        productList.value = products
    }.addOnFailureListener { e ->
        Log.e("Firestore", "Error getting products: ", e)
    }
}