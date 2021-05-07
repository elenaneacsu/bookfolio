package com.elenaneacsu.bookfolio.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elenaneacsu.bookfolio.utils.ResourceString

open class BaseViewModel(
    val resourceString: ResourceString,
    private val coroutineContextProvider: CoroutineContextProvider,
) : ViewModel() {
    val ioContext = coroutineContextProvider.IO

    var isRequestOk = false

    private val _fieldValidation = MutableLiveData<Boolean>(false)
    val fieldValidation: LiveData<Boolean>
        get() = _fieldValidation

    private val _progress = MutableLiveData(true)
    val progress: LiveData<Boolean>
        get() = _progress

    private val _isRefreshing = MutableLiveData(true)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    fun setError(message: String?) {
        _error.postValue(message)
    }

    protected fun showRefreshing() {
        _isRefreshing.value = true
    }

    protected fun hideRefreshing() {
        _isRefreshing.value = false
    }

    protected fun showProgress() {
        _progress.value = true
    }

    protected fun hideProgress() {
        _progress.value = false
    }

    protected fun enableFieldValidation() {
        _fieldValidation.postValue(true)
    }

    protected fun disableFieldValidation() {
        _fieldValidation.postValue(false)
    }

}