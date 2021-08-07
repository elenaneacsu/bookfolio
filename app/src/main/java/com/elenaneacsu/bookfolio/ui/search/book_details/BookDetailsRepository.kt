package com.elenaneacsu.bookfolio.ui.search.book_details

import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.ui.shelves.ShelvesRepository
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 01/06/21
 */
class BookDetailsRepository @Inject constructor(private val shelvesRepository: ShelvesRepository) :
    BaseRepository() {

    suspend fun getShelves() = shelvesRepository.getShelves()

    suspend fun addBookIntoShelf(book: UserBook, shelf: Shelf) {
        shelf.name?.let {
            book.item?.id?.let { bookId ->
                getMainDocumentOfRegisteredUser()?.collection(it)?.document(
                    bookId
                )?.set(book)?.await()
            }
        }
    }
}