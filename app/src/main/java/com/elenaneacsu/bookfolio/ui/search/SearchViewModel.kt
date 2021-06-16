package com.elenaneacsu.bookfolio.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.makeRequest
import com.elenaneacsu.bookfolio.models.google_books_api_models.FullItemResponse
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.utils.Constants.Companion.PLUS
import com.elenaneacsu.bookfolio.utils.Constants.Companion.SPACE
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 17/05/21
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val repository: SearchRepository
) : BaseViewModel(resourceString, coroutineContextProvider) {

    private val _booksSearchResult = MutableLiveData<Result<List<Item>>>()
    val booksSearchResult: LiveData<Result<List<Item>>>
        get() = _booksSearchResult

    private val _bookDetailsResult = MutableLiveData<Result<FullItemResponse>>()
    val bookDetailsResult: LiveData<Result<FullItemResponse>>
        get() = _bookDetailsResult

    private val _isBookClickHandled = MutableLiveData<Boolean>()
    val isBookClickHandled: LiveData<Boolean>
        get() = _isBookClickHandled

    fun searchBooks(searchTerm: String) =
        makeRequest(resourceString, ioContext, _booksSearchResult) {
            _booksSearchResult.postValue(Result.loading())
            val formattedSearchTerm = searchTerm.replace(SPACE, PLUS)
            val items = repository.searchBooks(formattedSearchTerm)
            val books = mutableListOf<Item>()
            if (items != null) {
                for (item in items)
                    books.add(item)
            }
            _booksSearchResult.postValue(Result.success(books.toList()))
        }

    fun getBookDetails(volumeId: String) =
        makeRequest(resourceString, ioContext, _bookDetailsResult) {
            _isBookClickHandled.postValue(false)
            _bookDetailsResult.postValue(Result.loading())
            val book = repository.getBooksDetails(volumeId)
            if (book != null) {
                _bookDetailsResult.postValue(Result.success(book))
                _isBookClickHandled.postValue(true)
            }

        }
}