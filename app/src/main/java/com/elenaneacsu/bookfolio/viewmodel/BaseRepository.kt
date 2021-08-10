package com.elenaneacsu.bookfolio.viewmodel

import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

open class BaseRepository : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val delay = async(Dispatchers.IO) { delay(700) }

    fun cleanup() {
        job.cancel()
    }

    fun getMainDocumentOfRegisteredUser() = auth.currentUser?.uid?.let {
        firestore.collection(Constants.USERS_COLLECTION).document(it)
    }

    suspend fun updateNumberOfBooksInShelfAsync(
        shelf: Shelf,
        isIncremented: Boolean
    ): Deferred<Void?> {
        val updatedValue = if (isIncremented) FieldValue.increment(1) else FieldValue.increment(-1)

        return async(coroutineContext) {
            shelf.name?.let { shelfName ->
                getMainDocumentOfRegisteredUser()?.collection(shelfName)
                    ?.document(Constants.NUMBER_OF_BOOKS)
                    ?.update(Constants.NUMBER_OF_BOOKS_FIELD_NAME, updatedValue)?.await()
            }
        }
    }

}