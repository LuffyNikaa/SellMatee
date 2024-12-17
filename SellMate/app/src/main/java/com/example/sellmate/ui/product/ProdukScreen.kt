package com.example.sellmate.ui.product



import HistoryViewModel
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sellmate.BottomNavigationBar
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
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Name: ${product.name}")
            Text("Category: ${product.category}")
            Text("Price: Rp ${product.price}")
            Text("Quantity: ${product.quantity}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Tombol Edit
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Tombol Delete dengan Ikon Sampah
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
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
