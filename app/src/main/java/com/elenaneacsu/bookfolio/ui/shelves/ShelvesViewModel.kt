package com.elenaneacsu.bookfolio.ui.shelves

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.makeRequest
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.ShelfType
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 07/05/21
 */
@HiltViewModel
class ShelvesViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val repository: ShelvesRepository
) : BaseViewModel(resourceString, coroutineContextProvider) {

    val username = repository.getUserName()
    var currentlyReadingShelf: Shelf? = null

    private val _shelvesResult =
        MutableLiveData<Result<Pair<List<Shelf>, List<BookDetailsMapper>>>>()
    val shelvesResult: LiveData<Result<Pair<List<Shelf>, List<BookDetailsMapper>>>>
        get() = _shelvesResult

    fun getShelvesScreenInfo() = makeRequest(resourceString, ioContext, _shelvesResult) {
        supervisorScope {
            _shelvesResult.postValue(Result.loading())

            val screenFullInfo = repository.getShelvesScreenInfo()
            currentlyReadingShelf =
                screenFullInfo.first.firstOrNull { it.name == ShelfType.CURRENTLY_READING.valueAsString }

            _shelvesResult.postValue(Result.success(screenFullInfo))
        }
    }

}