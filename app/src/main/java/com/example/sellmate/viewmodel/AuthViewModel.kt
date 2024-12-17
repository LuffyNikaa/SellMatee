package com.example.sellmate.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sellmate.data.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var sharedPreferences: SharedPreferences
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        checkLoginSession()
    }

    private fun checkLoginSession() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn && auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.matches("^(?=.[A-Za-z])(?=.\\d)(?=.[!@#\$%^&(),.?\":{}|<>]).{6,}$".toRegex())
    }

    fun login(email: String, password: String) {
        if (!isEmailValid(email)) {
            _authState.value = AuthState.Error("Invalid email format")
            return
        }
        if (!isPasswordValid(password)) {
            _authState.value = AuthState.Error("Password must include letters, numbers, and special characters")
            return
        }

        _authState.value = AuthState.loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveLoginSession(true)
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed. Please try again.")
                }
            }
    }

    fun signup(nama: String, email: String, password: String) {
        if (nama.isBlank() || !isEmailValid(email) || !isPasswordValid(password)) {
            _authState.value = AuthState.Error("Please provide valid data for all fields")
            return
        }

        _authState.value = AuthState.loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mapOf("name" to nama, "email" to email)
                    firestore.collection("users").document(auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            saveLoginSession(true)
                            _authState.value = AuthState.Authenticated
                        }
                        .addOnFailureListener {
                            auth.signOut()
                            _authState.value = AuthState.Error("Failed to save user data.")
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Signup failed. Please try again.")
                }
            }
    }


    fun logout() {
        auth.signOut()
        saveLoginSession(false)
        _authState.value = AuthState.Unauthenticated
    }

    private fun saveLoginSession(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }


    fun loadUserData(onDataLoaded: (UserData) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val userData = document.toObject(UserData::class.java) ?: UserData()
                onDataLoaded(userData)
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error("Failed to load user data")
            }
    }

    fun updateUserData(field: String, value: String) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .update(field, value)
            .addOnSuccessListener {
                if (field == "email") {
                    auth.currentUser?.updateEmail(value)
                } else if (field == "password") {
                    auth.currentUser?.updatePassword(value)
                }
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error("Failed to update $field")
            }
    }

}
sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object loading : AuthState()
    data class Error(val message : String) : AuthState()
}