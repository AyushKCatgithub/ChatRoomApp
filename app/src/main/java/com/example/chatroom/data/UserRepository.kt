package com.example.chatroom.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository (private val auth: FirebaseAuth,
                      private val firestore: FirebaseFirestore){
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
           val user = User(email, firstName, lastName)
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)

        }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    fun getCurrentUser(): Result<User> {
        return try {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                val user = User(
                    email = firebaseUser.email ?: "",
                    firstName = "", // You might want to fetch this from Firestore if needed
                    lastName = "" // Same as above
                )
                Result.Success(user)
            } else {
                Result.Error(Exception("No user currently logged in"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}