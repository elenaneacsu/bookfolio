package com.elenaneacsu.bookfolio.ui.shelves.book_journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.makeRequest
import com.elenaneacsu.bookfolio.models.*
import com.elenaneacsu.bookfolio.ui.search.book_details.BookDetailsRepository
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import com.elenaneacsu.bookfolio.viewmodel.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 22/08/2021
 */
@HiltViewModel
class JournalViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val repository: JournalRepository,
    private val bookDetailsRepository: BookDetailsRepository
) : BaseViewModel(
    resourceString, coroutineContextProvider
) {
    private val _addQuoteToJournalResult = MutableLiveData<Result<Quote>>()
    val addQuoteToJournalResult: LiveData<Result<Quote>>
        get() = _addQuoteToJournalResult

    private val _bookJournalResult = SingleLiveEvent<Result<BookJournal>>()
    val bookJournalResult: LiveData<Result<BookJournal>>
        get() = _bookJournalResult

    private val _updateQuoteResult = MutableLiveData<Result<Pair<Quote, Quote>>>()
    val updateQuoteResult: LiveData<Result<Pair<Quote, Quote>>>
        get() = _updateQuoteResult

    fun addQuoteToJournal(shelf: Shelf?, book: BookDetailsMapper?, text: String?) =
        makeRequest(resourceString, ioContext, _addQuoteToJournalResult) {
            if (shelf == null || book == null) {
                setError(resourceString.getString(R.string.default_error_message))
            } else if (text?.isEmpty() == true) {
                setError(resourceString.getString(R.string.empty_quote))
            } else {
                _addQuoteToJournalResult.postValue(Result.loading())
                repository.addQuoteToJournal(shelf, book, text!!)
                _addQuoteToJournalResult.postValue(Result.success(repository.quote))
            }
        }

    fun getBookJournal(shelf: Shelf?, book: BookDetailsMapper?) =
        makeRequest(resourceString, ioContext, _bookJournalResult) {
            _bookJournalResult.postValue(Result.loading())
            if (shelf == null || book == null) {
                setError(resourceString.getString(R.string.default_error_message))
            } else {
                val journal = book.userBook?.let { bookDetailsRepository.getBookJournal(shelf, it) }
                if (journal == null)
                    setError(resourceString.getString(R.string.book_journal_error))
                else
                    _bookJournalResult.postValue(Result.success(journal))
            }
        }

    fun updateQuoteData(
        shelf: Shelf?,
        book: BookDetailsMapper?,
        quote: Quote,
        key: QuoteKey,
        value: Any?
    ) =
        makeRequest(resourceString, ioContext, _updateQuoteResult) {
            if (shelf == null || book == null || value == null) {
                setError(resourceString.getString(R.string.default_error_message))
            } else if (key == QuoteKey.PAGE && (value as? String).isNullOrEmpty()) {
                setError(resourceString.getString(R.string.no_page_number_provided))
            } else if (key == QuoteKey.TEXT && (value as? String).isNullOrEmpty()) {
                setError(resourceString.getString(R.string.empty_quote))
            } else {
                _updateQuoteResult.postValue(Result.loading())

                repository.updateQuoteData(shelf, book, quote.id, key.valueAsString, value)

                repository.quote = when (key) {
                    QuoteKey.PAGE -> quote.copy(page = value as String)
                    QuoteKey.DATE -> quote.copy(date = value as Long)
                    else -> quote.copy(text = value as String)
                }

                //emit the old quote in order to find it in the adapter's list
                _updateQuoteResult.postValue(Result.success(Pair(quote, repository.quote)))
            }
        }
}