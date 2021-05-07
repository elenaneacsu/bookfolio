package com.elenaneacsu.bookfolio.extensions

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by Elena Neacsu on 05/05/21
 */

fun <T> BaseViewModel.makeRequest(
    resourceString: ResourceString,
    coroutineContext: CoroutineContext,
    resultMutableLiveData: MutableLiveData<Result<T>>,
    requestBlock: suspend () -> Unit,
) = viewModelScope.launch(coroutineContext) {
    try {
        requestBlock()
    } catch (exception: Exception) {
        Log.d("REQUEST", exception.message, exception)
        when (exception) {
            is FirebaseAuthInvalidUserException -> {
                resultMutableLiveData.postValue(
                    Result.error(
                        resourceString.getString(R.string.account_does_not_exist),
                        exception
                    )
                )
            }
            is FirebaseAuthInvalidCredentialsException -> {
                resultMutableLiveData.postValue(
                    Result.error(resourceString.getString(R.string.invalid_password), exception)
                )
            }
            is FirebaseNetworkException -> {
                resultMutableLiveData.postValue(
                    Result.error(
                        resourceString.getString(R.string.no_internet_connection),
                        exception
                    )
                )
            }
            is FirebaseFirestoreException -> {
                if (exception.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    resultMutableLiveData.postValue(
                        Result.error(
                            resourceString.getString(R.string.permission_denied),
                            exception
                        )
                    )
                }
            }

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
            else -> {
                resultMutableLiveData.postValue(
                    Result.error(
                        resourceString.getString(R.string.default_error_message),
                        exception
                    )
                )
            }
        }
    }
}

data class Result<out T>(
    val status: Status,
    val data: T?,
    val message: String? = null,
    val exception: Exception? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T? = null): Result<T> {
            return Result(
                Status.SUCCESS,
                data
            )
        }

        fun <T> error(message: String, exception: Exception? = null, data: T? = null): Result<T> {
            return Result(
                Status.ERROR,
                data,
                message,
                exception
            )
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(
                Status.LOADING,
                data
            )
        }
    }
}
