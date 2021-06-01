package com.elenaneacsu.bookfolio.ui.shelves

import com.elenaneacsu.bookfolio.utils.Constants.Companion.CURRENTLY_READING_COLLECTION
import com.elenaneacsu.bookfolio.utils.Constants.Companion.NUMBER_OF_BOOKS
import com.elenaneacsu.bookfolio.utils.Constants.Companion.READ_COLLECTION
import com.elenaneacsu.bookfolio.utils.Constants.Companion.TO_READ_COLLECTION
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 08/05/21
 */
class ShelvesRepository @Inject constructor() : BaseRepository() {

    fun getUserName() = auth.currentUser?.displayName ?: "empty"

    suspend fun getShelves(): List<DocumentSnapshot?> {
        val toReadShelf = async(Dispatchers.IO) { getToReadCollection() }
        val currentlyReadingShelf = async(Dispatchers.IO) { getCurrentlyReadingCollection() }
        val readShelf = async(Dispatchers.IO) {
            getReadCollection()
        }
        val delay = async(Dispatchers.IO) { delay(500) }


        return awaitAll(toReadShelf, currentlyReadingShelf, readShelf, delay).filterIsInstance(DocumentSnapshot::class.java)
    }

    private suspend fun getToReadCollection() =
        getMainDocumentOfRegisteredUser()
            ?.collection(TO_READ_COLLECTION)
            ?.document(NUMBER_OF_BOOKS)?.get()?.await()

    private suspend fun getCurrentlyReadingCollection() = getMainDocumentOfRegisteredUser()
        ?.collection(CURRENTLY_READING_COLLECTION)
        ?.document(NUMBER_OF_BOOKS)?.get()?.await()

    private suspend fun getReadCollection() = getMainDocumentOfRegisteredUser()
        ?.collection(READ_COLLECTION)
        ?.document(NUMBER_OF_BOOKS)?.get()?.await()

}