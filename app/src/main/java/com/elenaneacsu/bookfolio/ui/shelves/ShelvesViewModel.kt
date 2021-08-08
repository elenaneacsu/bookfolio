package com.elenaneacsu.bookfolio.ui.shelves

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.makeRequest
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _shelvesResult = MutableLiveData<Result<List<Shelf>>>()
    val shelvesResult: LiveData<Result<List<Shelf>>>
        get() = _shelvesResult

    fun getShelves() = makeRequest(resourceString, ioContext, _shelvesResult) {
        _shelvesResult.postValue(Result.loading())

        val shelves = repository.getShelves()
        _shelvesResult.postValue(Result.success(shelves))
    }

}