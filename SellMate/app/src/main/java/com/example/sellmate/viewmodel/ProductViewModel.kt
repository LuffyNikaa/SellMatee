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
            .add(newProduct)
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
                    val product = document.toObject(Product::class.java)
                    product?.copy(id = document.id) // Menyertakan ID dokumen
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

        db.collection("products")
            .whereEqualTo("name", productName)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.e("ProductViewModel", "Product not found")
                    return@addOnSuccessListener
                }

                for (document in result) {
                    db.collection("products")
                        .document(document.id)
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

    // Fungsi untuk menandai produk sebagai "tidak baru"
    fun markAsOld(productId: String) {
        db.collection("products")
            .document(productId)
            .update("isNew", false)
            .addOnSuccessListener {
                Log.d("ProductViewModel", "Product marked as old successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("ProductViewModel", "Error marking product as old: ${e.message}")
            }
    }

    // Fungsi untuk memperbarui produk di Firestore
    fun updateProduct(product: Product) {
        if (product.id.isEmpty()) {
            Log.e("ProductViewModel", "Invalid product ID")
            return
        }

        db.collection("products")
            .document(product.id)
            .set(product)
            .addOnSuccessListener {
                Log.d("ProductViewModel", "Produk berhasil diperbarui.")
            }
            .addOnFailureListener { e ->
                Log.e("ProductViewModel", "Gagal memperbarui produk: ${e.message}")
            }
    }
}
