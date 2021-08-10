package com.elenaneacsu.bookfolio.ui.shelves

import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.ShelfType
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.utils.Constants
import com.elenaneacsu.bookfolio.utils.Constants.Companion.NUMBER_OF_BOOKS
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 08/05/21
 */
class ShelvesRepository @Inject constructor() : BaseRepository() {

    fun getUserName() = auth.currentUser?.displayName ?: "empty"

    suspend fun getShelvesScreenInfo(): Pair<List<Shelf>, List<BookDetailsMapper>> {
        val shelves = getShelves()
        val currentlyReadingBooks = getCurrentlyReadingBooks()

        return Pair(shelves.toList(), currentlyReadingBooks.toList())
    }

    suspend fun getShelves(): List<Shelf> {
        val toReadShelfDeferred = getToReadCollectionAsync()
        val currentlyReadingShelfDeferred = getCurrentlyReadingCollectionAsync()
        val readShelfDeferred = getReadCollectionAsync()

        val shelvesSnapshots = awaitAll(
            toReadShelfDeferred,
            currentlyReadingShelfDeferred,
            readShelfDeferred,
            delay
        ).filterIsInstance(
            DocumentSnapshot::class.java
        )

        val shelves = mutableListOf<Shelf>()
        shelves.addAll(shelvesSnapshots.mapNotNull { documentSnapshot ->
            documentSnapshot.toObject(
                Shelf::class.java
            )
        })

        return shelves.toList()
    }

    private suspend fun getCurrentlyReadingBooks(): List<BookDetailsMapper> {
        val currentlyReadingBooksDeferred = getCurrentlyReadingBooksAsync()

        val currentlyReadingBooksSnapshot = currentlyReadingBooksDeferred.await()
        val currentlyReadingBooks = mutableListOf<BookDetailsMapper>()
        currentlyReadingBooksSnapshot?.documents?.filterNot { it.id == Constants.NUMBER_OF_BOOKS }
            ?.let {
                currentlyReadingBooks.addAll(
                    it.mapNotNull { documentSnapshot ->
                        BookDetailsMapper(
                            userBook = documentSnapshot.toObject(
                                UserBook::class.java
                            )
                        )
                    })
            }

        return currentlyReadingBooks.toList()
    }

    private suspend fun getCurrentlyReadingBooksAsync() = async(coroutineContext) {
        getMainDocumentOfRegisteredUser()?.collection(ShelfType.CURRENTLY_READING.valueAsString)
            ?.get()?.await()
    }

    private suspend fun getToReadCollectionAsync() = async(coroutineContext) {
        getMainDocumentOfRegisteredUser()
            ?.collection(ShelfType.TO_READ.valueAsString)
            ?.document(NUMBER_OF_BOOKS)?.get()?.await()
    }

    private suspend fun getCurrentlyReadingCollectionAsync() = async(coroutineContext) {
        getMainDocumentOfRegisteredUser()
            ?.collection(ShelfType.CURRENTLY_READING.valueAsString)
            ?.document(NUMBER_OF_BOOKS)?.get()?.await()
    }

    private suspend fun getReadCollectionAsync() = async(coroutineContext) {
        getMainDocumentOfRegisteredUser()
            ?.collection(ShelfType.READ.valueAsString)
            ?.document(NUMBER_OF_BOOKS)?.get()?.await()
    }

}