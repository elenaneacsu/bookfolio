package com.elenaneacsu.bookfolio.ui.search.book_details

import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.ui.shelves.ShelvesRepository
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 01/06/21
 */
class BookDetailsRepository @Inject constructor(private val shelvesRepository: ShelvesRepository) :
    BaseRepository() {

    suspend fun getShelves() = shelvesRepository.getShelves()

    suspend fun processAddBookIntoShelf(book: UserBook, shelf: Shelf) {
        val bookAddedIntoShelfDeferred = addBookIntoShelfAsync(book, shelf)
        val numberOfBooksInShelfDeferred =
            updateNumberOfBooksInShelfAsync(shelf, isIncremented = true)

        awaitAll(bookAddedIntoShelfDeferred, numberOfBooksInShelfDeferred)
    }

    private suspend fun addBookIntoShelfAsync(book: UserBook, shelf: Shelf): Deferred<Void?> {
        return async(Dispatchers.IO) {
            shelf.name?.let { shelfName ->
                book.item?.id?.let { bookId ->
                    getMainDocumentOfRegisteredUser()?.collection(shelfName)?.document(
                        bookId
                    )?.set(book)?.await()
                }
            }
        }
    }
}