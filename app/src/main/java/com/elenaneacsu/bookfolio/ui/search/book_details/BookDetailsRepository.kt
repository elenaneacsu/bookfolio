package com.elenaneacsu.bookfolio.ui.search.book_details

import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.ui.shelves.ShelvesRepository
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
class BookDetailsRepository @Inject constructor(private val shelvesRepository: ShelvesRepository) :
    BaseRepository() {

    suspend fun getShelves() = shelvesRepository.getShelves()

    suspend fun addBookIntoShelf(book: UserBook, shelf: Shelf) {
        shelf.name?.let { book.item?.id?.let { it1 ->
            getMainDocumentOfRegisteredUser()?.collection(it)?.document(
                it1
            )?.set(book)?.await()
        } }
    }
}