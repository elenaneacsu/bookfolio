package com.elenaneacsu.bookfolio.ui.shelves.shelf

import com.elenaneacsu.bookfolio.models.ShelfType
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.networking.RetrofitRequests
import com.elenaneacsu.bookfolio.utils.Constants
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 15/05/21
 */
class ShelfRepository @Inject constructor(private val retrofitRequests: RetrofitRequests) :
    BaseRepository() {

    suspend fun getBooksInShelf(shelfType: ShelfType): List<UserBook> {
        val booksInShelfQueryDeferred = async(Dispatchers.IO) {
            getMainDocumentOfRegisteredUser()?.collection(shelfType.valueAsString)?.get()?.await()
        }

        val booksInShelfQuery = awaitAll(booksInShelfQueryDeferred, delay).filterIsInstance(
            QuerySnapshot::class.java
        )[0]

        val books = mutableListOf<UserBook>()
        books.addAll(booksInShelfQuery.documents.filterNot { it.id == Constants.NUMBER_OF_BOOKS }
            .mapNotNull { it.toObject(UserBook::class.java) })

        return books.toList()
    }
}