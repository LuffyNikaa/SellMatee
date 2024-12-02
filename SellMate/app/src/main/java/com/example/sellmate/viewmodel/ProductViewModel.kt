package com.example.sellmate.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sellmate.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Fungsi untuk menambahkan produk ke Firestore
    fun addProduct(product: Product) {
        val newProduct = product.copy(isNew = true)
        db.collection("products")
            .add(product)
            .addOnSuccessListener {
                Log.d("ProductViewModel", "Product added successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("ProductViewModel", "Failed to add product: ${e.message}")
            }
    }

    // Fungsi untuk mendapatkan produk dari Firestore
    fun getProducts(onProductsLoaded: (List<Product>) -> Unit) {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.documents.mapNotNull { document ->
                    document.toObject(Product::class.java)
                }
                onProductsLoaded(products)
            }
            .addOnFailureListener { e ->
                Log.e("ProductViewModel", "Error getting products: ${e.message}")
            }
    }

    // Fungsi untuk menghapus produk dari Firestore
    fun deleteProductByName(productName: String) {
        if (productName.isEmpty()) {
            Log.e("ProductViewModel", "Invalid product name")
            return
        }

        // Mencari produk berdasarkan nama
        db.collection("products")
            .whereEqualTo("name", productName)  // Mencocokkan nama produk
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.e("ProductViewModel", "Product not found")
                    return@addOnSuccessListener
                }

                // Hapus dokumen yang ditemukan
                for (document in result) {
                    db.collection("products")
                        .document(document.id)  // Mengakses dokumen berdasarkan ID
                        .delete()
                        .addOnSuccessListener {
                            Log.d("ProductViewModel", "Product deleted successfully.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("ProductViewModel", "Error deleting product: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ProductViewModel", "Error querying product: ${e.message}")
            }
    }
    fun markAsOld(productId: String) {
        db.collection("products")
            .document(productId)
            .update("isNew", false)
            .addOnSuccessListener {
                // Logika jika berhasil diperbarui
            }
            .addOnFailureListener { e ->
                // Tangani error jika gagal
                e.printStackTrace()
            }
    }
}
