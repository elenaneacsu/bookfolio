package com.elenaneacsu.bookfolio.ui.search.book_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.makeRequest
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.utils.date.toStringDate
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 01/06/21
 */
@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val repository: BookDetailsRepository
) : BaseViewModel(
    resourceString, coroutineContextProvider
) {

    private val _shelvesResult = MutableLiveData<Result<List<Shelf>>>()
    val shelvesResult: LiveData<Result<List<Shelf>>>
        get() = _shelvesResult

    private val _addBookResult = MutableLiveData<Result<Void>>()
    val addBookResult: LiveData<Result<Void>>
        get() = _addBookResult

    fun getShelves() = makeRequest(resourceString, ioContext, _shelvesResult) {
        _shelvesResult.postValue(Result.loading())
        val docSnapshotsList = repository.getShelves()
        val shelves = mutableListOf<Shelf>()
        for (docSnapshot in docSnapshotsList) {
            docSnapshot?.toObject(Shelf::class.java)?.let {
                shelves.add(it)
            }
        }
        _shelvesResult.postValue(Result.success(shelves))
    }

    fun addBookIntoShelf(book: Item, shelf: Shelf) = makeRequest(resourceString, ioContext, _addBookResult) {
        _addBookResult.postValue(Result.loading())
        repository.addBookIntoShelf(
            UserBook(item = book, startDate = Date().time.toStringDate()),
            shelf
        )
        _addBookResult.postValue(Result.success())
    }
}