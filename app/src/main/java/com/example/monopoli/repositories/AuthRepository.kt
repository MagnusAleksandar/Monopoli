package com.example.monopoli.repositories


import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun login(email: String, password: String): Result<Pair<String, String>> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            val userId = user?.uid ?: ""
            val userEmail = user?.email ?: "Usuario desconocido"
            Result.success(Pair(userId, userEmail))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): Pair<String, String>? {
        val user = auth.currentUser
        return user?.let { Pair(it.uid, it.email ?: "") }
    }
}