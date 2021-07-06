package com.elenaneacsu.bookfolio.ui.auth

import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.ShelfType
import com.elenaneacsu.bookfolio.models.User
import com.elenaneacsu.bookfolio.utils.Constants.Companion.CURRENTLY_READING_COLLECTION
import com.elenaneacsu.bookfolio.utils.Constants.Companion.NUMBER_OF_BOOKS
import com.elenaneacsu.bookfolio.utils.Constants.Companion.READ_COLLECTION
import com.elenaneacsu.bookfolio.utils.Constants.Companion.TO_READ_COLLECTION
import com.elenaneacsu.bookfolio.utils.Constants.Companion.USERS_COLLECTION
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() : BaseRepository() {

    suspend fun login(email: String, password: String): AuthResult? =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun signup(name: String, email: String, password: String) {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        if (authResult.user != null) {
            val isUserAdded =
                async(Dispatchers.IO) { addNewUser(User(name, email), authResult.user) }
            val isNameUpdated = async(Dispatchers.IO) { updateUserDisplayName(name) }

            awaitAll(isUserAdded, isNameUpdated)
            createCollections()
        }
    }

    suspend fun forgotPassword(email:String) {
        auth.sendPasswordResetEmail(email).await()
    }

    private suspend fun createCollections() {
        val isCurrentlyReadingColCreated =
            async(Dispatchers.IO) { createCurrentlyReadingCollection() }
        val isToReadColCreated = async(Dispatchers.IO) { createToReadCollection() }
        val isReadColCreated = async(Dispatchers.IO) { createReadCollection() }

        awaitAll(isCurrentlyReadingColCreated, isToReadColCreated, isReadColCreated)
    }

    private suspend fun createCurrentlyReadingCollection() =
        auth.currentUser?.let {
            firestore.collection(USERS_COLLECTION)
                .document(it.uid)
                .collection(ShelfType.CURRENTLY_READING.valueAsString)
                .document(NUMBER_OF_BOOKS)
                .set(setUpShelfDoc(ShelfType.CURRENTLY_READING))
                .await()
        }

    private suspend fun createToReadCollection() =
        auth.currentUser?.let {
            firestore.collection(USERS_COLLECTION)
                .document(it.uid)
                .collection(ShelfType.TO_READ.valueAsString)
                .document(NUMBER_OF_BOOKS)
                .set(setUpShelfDoc(ShelfType.TO_READ))
                .await()
        }

    private suspend fun createReadCollection() =
        auth.currentUser?.let {
            firestore.collection(USERS_COLLECTION)
                .document(it.uid)
                .collection(ShelfType.READ.valueAsString)
                .document(NUMBER_OF_BOOKS)
                .set(setUpShelfDoc(ShelfType.READ))
                .await()
        }

    private fun updateUserDisplayName(name: String) =
        auth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(name).build()
        )

    private fun addNewUser(user: User, firebaseUser: FirebaseUser) =
        firestore.collection(USERS_COLLECTION)
            .document(firebaseUser.uid)
            .set(user)

    private fun setUpShelfDoc(shelfType: ShelfType) = Shelf(shelfType.valueAsString, 0)
}