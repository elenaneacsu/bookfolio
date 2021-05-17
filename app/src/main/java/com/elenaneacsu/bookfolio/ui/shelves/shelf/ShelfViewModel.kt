package com.elenaneacsu.bookfolio.ui.shelves.shelf

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.makeRequest
import com.elenaneacsu.bookfolio.models.google_books_api_models.VolumeInfo
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

    private val _mla = MutableLiveData<Result<List<VolumeInfo>>>()
    val mla: LiveData<Result<List<VolumeInfo>>>
        get() = _mla

    fun makeTestRequest(searchTerm: String) = makeRequest(resourceString, ioContext, _mla) {
        val items = repository.searchBooks(searchTerm)
        if (items != null) {
            for (item in items) {
                Log.d("TAG", "makeTestRequest: " + item.volumeInfo?.title)
            }
        }
    }
}