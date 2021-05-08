package com.elenaneacsu.bookfolio.ui.auth

import com.elenaneacsu.bookfolio.models.User
import com.elenaneacsu.bookfolio.utils.Constants.Companion.USERS_COLLECTION
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() : BaseRepository() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): AuthResult? =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun signup(name: String, email: String, password: String) {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        if (authResult.user != null) {
            val isUserAdded = async(Dispatchers.IO) { addNewUser(User(name, email)) }
            val isNameUpdated = async(Dispatchers.IO) { updateUserDisplayName(name) }
            awaitAll(isUserAdded, isNameUpdated)
        }
    }

    private suspend fun updateUserDisplayName(name: String) =
        auth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(name).build()
        )?.await()


    private suspend fun addNewUser(user: User) =
        firestore.collection(USERS_COLLECTION)
            .document()
            .set(user)
            .await()

}