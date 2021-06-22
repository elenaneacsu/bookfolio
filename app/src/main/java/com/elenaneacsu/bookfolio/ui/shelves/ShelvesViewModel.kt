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

    private val _mla = MutableLiveData<Result<List<Shelf>>>()
    val mla: LiveData<Result<List<Shelf>>>
        get() = _mla

    fun getShelves() = makeRequest(resourceString, ioContext, _mla) {
        _mla.postValue(Result.loading())
        val docSnapshotsList = repository.getShelves()
        val shelves = mutableListOf<Shelf>()
        for (docSnapshot in docSnapshotsList) {
            docSnapshot?.toObject(Shelf::class.java)?.let {
                if(it.name?.equals("To read", ignoreCase = true) == true)
                    it.numberOfBooks = 10
                else if (it.name?.equals("Read", ignoreCase = true) == true)
                    it.numberOfBooks = 5
                else
                    it.numberOfBooks = 2
                shelves.add(it)
            }
        }
        _mla.postValue(Result.success(shelves))
    }

}