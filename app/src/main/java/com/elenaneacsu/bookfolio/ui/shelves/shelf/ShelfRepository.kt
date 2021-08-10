package com.elenaneacsu.bookfolio.ui.shelves.shelf

import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.ShelfType
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.utils.Constants
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 15/05/21
 */
class ShelfRepository @Inject constructor() : BaseRepository() {

    suspend fun getBooksInShelf(shelf: Shelf): List<UserBook> {
        val shelfTypeName = ShelfType.getShelfType(shelf.name!!)?.valueAsString
        val booksInShelfQueryDeferred = async(coroutineContext) {
            shelfTypeName?.let { getMainDocumentOfRegisteredUser()?.collection(it)?.get()?.await() }
        }

        val booksInShelfQuery = awaitAll(booksInShelfQueryDeferred, delay).filterIsInstance(
            QuerySnapshot::class.java
        )[0]

        val books = mutableListOf<UserBook>()
        books.addAll(booksInShelfQuery.documents.filterNot { it.id == Constants.NUMBER_OF_BOOKS }
            .mapNotNull { it.toObject(UserBook::class.java) })

        return books.toList()
    }

    suspend fun processRemoveBookFromShelf(book: BookDetailsMapper, shelf: Shelf) {
        val bookRemovedFromShelfDeferred = removeBookFromShelfAsync(book, shelf)
        val numberOfBooksInShelfDeferred =
            updateNumberOfBooksInShelfAsync(shelf, isIncremented = false)

        awaitAll(bookRemovedFromShelfDeferred, numberOfBooksInShelfDeferred)
    }

    private suspend fun removeBookFromShelfAsync(
        book: BookDetailsMapper,
        shelf: Shelf
    ): Deferred<Void?> {
        val shelfTypeName = ShelfType.getShelfType(shelf.name!!)?.valueAsString

        return async(coroutineContext) {
            shelfTypeName?.let { shelfName ->
                book.getId()?.let { bookId ->
                    getMainDocumentOfRegisteredUser()?.collection(shelfName)?.document(
                        bookId
                    )?.delete()?.await()
                }
            }
        }
    }
}