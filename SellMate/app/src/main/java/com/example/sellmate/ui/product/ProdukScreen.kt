package com.example.sellmate.ui.product



import HistoryViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sellmate.BottomNavigationBar
import com.example.sellmate.R
import com.example.sellmate.data.model.Product



import com.example.sellmate.viewmodel.ProductViewModel


@Composable
fun ProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    historyViewModel: HistoryViewModel
) {
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    // Untuk menangani dialog konfirmasi
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    // Mendapatkan produk dari Firestore secara real-time
    LaunchedEffect(Unit) {
        productViewModel.getProducts { products ->
            productList = products
        }
    }

    // Filter produk berdasarkan pencarian
    val filteredProducts = if (searchQuery.isEmpty()) {
        productList
    } else {
        productList.filter { product ->
            product.name.contains(searchQuery, ignoreCase = true) || product.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchBar(searchQuery = searchQuery, onSearchQueryChange = { searchQuery = it })
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 1, // Menandakan tab produk aktif
                onItemSelected = { selectedIndex -> },
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(filteredProducts) { product ->
                ProductCard(
                    product = product,
                    onDeleteClick = {
                        productToDelete = product // Menyimpan produk yang akan dihapus
                        showDeleteDialog = true // Menampilkan dialog konfirmasi
                    },
                    onEditClick = {
                        navController.navigate("editProduct/${product.id}")
                    }
                )
            }
        }
    }

    // Dialog konfirmasi penghapusan produk
    if (showDeleteDialog && productToDelete != null) {
        DeleteConfirmationDialog(
            product = productToDelete!!,
            onDismiss = { showDeleteDialog = false },
            onConfirmDelete = {
                productToDelete?.name?.let { productName ->
                    // Memanggil fungsi deleteProductByName dengan nama produk
                    productViewModel.deleteProductByName(productName)
                    // Menambahkan riwayat penghapusan produk
                    historyViewModel.addHistory("Produk $productName telah dihapus pada ${formatTimestamp(System.currentTimeMillis())}")
                    // Memperbarui daftar produk setelah penghapusan
                    productViewModel.getProducts { products ->
                        productList = products
                    }
                }
                showDeleteDialog = false
            }
        )
    }

    // Menambahkan riwayat penambahan produk jika ada produk baru yang ditambahkan
    LaunchedEffect(productList) {
        productList.forEach { product ->
            if (product.isNew) {
                historyViewModel.addHistory("Produk ${product.name} telah ditambahkan pada ${formatTimestamp(System.currentTimeMillis())}")
                productViewModel.markAsOld(product.id) // Tandai produk sebagai "tidak baru"
            }
        }
    }

// Dialog penghapusan produk
    if (showDeleteDialog && productToDelete != null) {
        DeleteConfirmationDialog(
            product = productToDelete!!,
            onDismiss = { showDeleteDialog = false },
            onConfirmDelete = {
                productToDelete?.name?.let { productName ->
                    productViewModel.deleteProductByName(productName)
                    historyViewModel.addHistory("Produk $productName telah dihapus pada ${formatTimestamp(System.currentTimeMillis())}")
                }
                showDeleteDialog = false
            }
        )
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Search Products...") },
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
    }
}

@Composable
fun ProductCard(
    product: Product,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(1f) // Mengurangi lebar menjadi 90% dari lebar layar
            .padding(16.dp), // Memberikan padding pada card
        shape = RoundedCornerShape(16.dp), // Ganti sudut menjadi lebih melengkung
        colors = CardDefaults.cardColors(containerColor = Color(0xFF7C93C3)) // Warna latar belakang
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Baris utama untuk gambar produk dan informasi
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Gambar produk lebih besar dan sudut melengkung
                Image(
                    painter = painterResource(id = R.drawable.placeholder), // Ganti dengan resource gambar
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(100.dp) // Gambar lebih kecil
                        .clip(RoundedCornerShape(12.dp)) // Gambar dengan sudut melengkung
                        .padding(end = 16.dp)
                )

                // Informasi produk dengan susunan vertikal
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Name: ${product.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(
                        text = "Category: ${product.category}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Text(
                        text = "Price: Rp ${product.price}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Text(
                        text = "Quantity: ${product.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol Edit dan Delete, letakkan di kanan bawah
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End // Mengatur tombol ke kanan
            ) {
                // Tombol Edit
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.White
                    )
                }

                // Tombol Delete
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Konfirmasi") },
        text = { Text("Apakah Anda yakin ingin menghapus produk: ${product.name}?") },
        confirmButton = {
            TextButton(onClick = onConfirmDelete) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Tidak")
            }
        }
    )
}
