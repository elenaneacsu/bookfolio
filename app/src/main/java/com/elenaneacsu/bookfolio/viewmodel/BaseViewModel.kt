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

//    fun <T> BaseViewModel.makeRequest(
//        resourceString: ResourceString,
//        coroutineContext: CoroutineContext,
//        resultMutableLiveData: MutableLiveData<Result<T>>,
//        requestBlock: suspend () -> Unit,
//    ) = viewModelScope.launch(coroutineContext) {
//        try {
//            requestBlock()
//        } catch (exception: Exception) {
//            Log.d("REQUEST", exception.message, exception)
//            when (exception) {
//                is NetworkException -> {
//                    resultMutableLiveData.postValue(
//                        Result.error(
//                            resourceString.getString(R.string.no_internet_connection),
//                            exception
//                        )
//                    )
//                }
//                is GenericApiError -> {
//                    resultMutableLiveData.postValue(
//                        Result.error(
//                            exception.message
//                                ?: resourceString.getString(R.string.default_error_message), exception
//                        )
//                    )
//                }
//                is HttpException -> {
//                    if (exception.code() == 500) {
//                        resultMutableLiveData.postValue(
//                            Result.error(
//                                resourceString.getString(R.string.default_error_message),
//                                exception
//                            )
//                        )
//                    } else {
//                        val errorBody = exception.response()?.errorBody()?.charStream()?.readText()
//                        val errorResponse = errorBody?.let { Gson().fromJson<ErrorMessageResponse>(it) }
//                        resultMutableLiveData.postValue(
//                            Result.error(
//                                errorResponse?.error
//                                    ?: errorResponse?.message
//                                    ?: resourceString.getString(R.string.default_error_message),
//                                exception
//                            )
//                        )
//                    }
//                }
//                is SSLException, is UnknownHostException -> {
//                    resultMutableLiveData.postValue(
//                        Result.error(
//                            resourceString.getString(R.string.check_internet_connection),
//                            exception
//                        )
//                    )
//                }
//                is NoLocationException -> {
//                    resultMutableLiveData.postValue(
//                        Result.error(
//                            "No location exception",
//                            exception
//                        )
//                    )
//                }
//                else -> {
//                    resultMutableLiveData.postValue(
//                        Result.error(
//                            resourceString.getString(R.string.default_error_message),
//                            exception
//                        )
//                    )
//                }
//            }
//        }
//    }
}