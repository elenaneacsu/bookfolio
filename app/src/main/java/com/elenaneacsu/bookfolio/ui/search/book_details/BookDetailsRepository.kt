package com.elenaneacsu.bookfolio.ui.search.book_details

import com.elenaneacsu.bookfolio.utils.Constants
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 01/06/21
 */
class BookDetailsRepository @Inject constructor() : BaseRepository() {

    suspend fun getShelves(): List<DocumentSnapshot?> {
        val toReadShelf = async(Dispatchers.IO) { getToReadCollection() }
        val currentlyReadingShelf = async(Dispatchers.IO) { getCurrentlyReadingCollection() }
        val readShelf = async(Dispatchers.IO) {
            getReadCollection()
        }
        val delay = async(Dispatchers.IO) { delay(500) }


        return awaitAll(toReadShelf, currentlyReadingShelf, readShelf, delay).filterIsInstance(
            DocumentSnapshot::class.java)
    }

    private suspend fun getToReadCollection() =
        getMainDocumentOfRegisteredUser()
            ?.collection(Constants.TO_READ_COLLECTION)
            ?.document(Constants.NUMBER_OF_BOOKS)?.get()?.await()

    private suspend fun getCurrentlyReadingCollection() = getMainDocumentOfRegisteredUser()
        ?.collection(Constants.CURRENTLY_READING_COLLECTION)
        ?.document(Constants.NUMBER_OF_BOOKS)?.get()?.await()

    private suspend fun getReadCollection() = getMainDocumentOfRegisteredUser()
        ?.collection(Constants.READ_COLLECTION)
        ?.document(Constants.NUMBER_OF_BOOKS)?.get()?.await()
}