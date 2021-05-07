package com.elenaneacsu.bookfolio.ui.auth

import com.elenaneacsu.bookfolio.models.User
import com.elenaneacsu.bookfolio.utils.Constants.Companion.USERS_COLLECTION
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() : BaseRepository() {

    private var auth: FirebaseAuth = Firebase.auth
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): AuthResult? =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun signup(name: String, email: String, password: String) {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        if (authResult.user != null)
            addNewUser(User(name, email))
    }

    private suspend fun addNewUser(user: User) {
        firestore.collection(USERS_COLLECTION)
            .document()
            .set(user)
            .await()
    }
}