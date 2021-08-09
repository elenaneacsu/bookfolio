package com.elenaneacsu.bookfolio.ui.shelves.shelf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.makeRequest
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 15/05/21
 */
@HiltViewModel
class ShelfViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val repository: ShelfRepository
) : BaseViewModel(resourceString, coroutineContextProvider) {

    private val _booksInShelfResult = MutableLiveData<Result<List<BookDetailsMapper>>>()
    val booksInShelfResult: LiveData<Result<List<BookDetailsMapper>>>
        get() = _booksInShelfResult

    private val _removeBookResult = MutableLiveData<Result<BookDetailsMapper>>()
    val removeBookResult: LiveData<Result<BookDetailsMapper>>
        get() = _removeBookResult

    fun getBooks(shelf: Shelf) = makeRequest(resourceString, ioContext, _booksInShelfResult) {
        _booksInShelfResult.postValue(Result.loading())

        val books = repository.getBooksInShelf(shelf).map { BookDetailsMapper(userBook = it) }

        _booksInShelfResult.postValue(Result.success(books))
    }

    fun removeBook(book: BookDetailsMapper, shelf: Shelf) =
        makeRequest(resourceString, ioContext, _removeBookResult) {
            _removeBookResult.postValue(Result.loading())

            repository.processRemoveBookFromShelf(book, shelf)

            _removeBookResult.postValue(Result.success(book))
        }
}