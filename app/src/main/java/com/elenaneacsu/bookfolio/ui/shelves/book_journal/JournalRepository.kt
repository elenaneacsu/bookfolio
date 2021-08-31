package com.elenaneacsu.bookfolio.ui.shelves.book_journal

import com.elenaneacsu.bookfolio.models.*
import com.elenaneacsu.bookfolio.room.QuoteDao
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 29/08/2021
 */
class JournalRepository @Inject constructor(private val quoteDao: QuoteDao) : BaseRepository() {

    lateinit var quote: Quote

    suspend fun addQuoteToJournal(shelf: Shelf, book: BookDetailsMapper, text: String) {

        val shelfTypeName = shelf.getShelfTypeName()

        quote = Quote(
            text = text,
            book = book.getTitle(),
            author = book.getAuthors(),
            date = Calendar.getInstance().timeInMillis
        )

        shelfTypeName?.let { shelfName ->
            book.getId()?.let { bookId ->
                getMainDocumentOfRegisteredUser()?.collection(shelfName)?.document(bookId)
                    ?.collection("journal")?.document(quote.id)?.set(quote)?.await()
            }
        }
    }

    suspend fun updateQuoteData(
        shelfName: String,
        bookId: String,
        quoteId: String,
        fieldName: String,
        fieldValue: Any
    ) {
        getMainDocumentOfRegisteredUser()?.collection(shelfName)?.document(bookId)
            ?.collection("journal")?.document(quoteId)?.update(fieldName, fieldValue)
            ?.await()
    }

    suspend fun removeQuote(
        shelfTypeName: String,
        bookId: String,
        quoteId: String
    ) {
        getMainDocumentOfRegisteredUser()?.collection(shelfTypeName)?.document(
            bookId
        )?.collection("journal")?.document(quoteId)?.delete()?.await()
    }

    suspend fun processAddQuoteToFavourites(shelfName: String, bookId: String, quote: Quote) {
        addQuoteToFavourites(shelfName, bookId, quote)
        updateQuoteData(shelfName, bookId, quote.id, "favourite", true)
    }

    suspend fun processRemoveQuoteFromFavourites(shelfName: String, bookId: String, quote: Quote) {
        val removeQuoteFromFavouritesDeferred = async { removeQuoteFromFavourites(quote) }
        val updateQuoteInFirestoreDeferred =
            async { updateQuoteData(shelfName, bookId, quote.id, "favourite", false) }

        awaitAll(removeQuoteFromFavouritesDeferred, updateQuoteInFirestoreDeferred)
    }

    suspend fun addQuoteToFavourites(shelfName: String, bookId: String, quote: Quote) {
        val quoteRoomEntity = QuoteRoomEntity(
            id = quote.id,
            text = quote.text,
            book = quote.book,
            author = quote.author,
            page = quote.page,
            date = quote.date,
            shelfName = shelfName,
            bookId = bookId
        )
        quoteDao.insertQuote(quoteRoomEntity)
    }

    fun getFavouriteQuotes() = quoteDao.getAllQuotes()

    private suspend fun removeQuoteFromFavourites(quote: Quote) {
        quoteDao.deleteQuote(quote.id)
    }

}