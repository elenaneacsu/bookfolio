package com.elenaneacsu.bookfolio.ui.shelves.book_journal

import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.Quote
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.ShelfType
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 29/08/2021
 */
class JournalRepository @Inject constructor() : BaseRepository() {

    lateinit var quote: Quote

    suspend fun addQuoteToJournal(shelf: Shelf, book: BookDetailsMapper, text: String) {

        val shelfTypeName = shelf.name?.let { shelfName ->
            ShelfType.getShelfType(
                shelfName
            )?.valueAsString
        }

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
        shelf: Shelf,
        book: BookDetailsMapper,
        quoteId: String,
        fieldName: String,
        fieldValue: Any
    ) {
        val shelfTypeName = shelf.name?.let { shelfName ->
            ShelfType.getShelfType(
                shelfName
            )?.valueAsString
        }

        shelfTypeName?.let { shelfName ->
            book.getId()?.let { bookId ->
                getMainDocumentOfRegisteredUser()?.collection(shelfName)?.document(bookId)
                    ?.collection("journal")?.document(quoteId)?.update(fieldName, fieldValue)
                    ?.await()
            }
        }
    }
}