package com.elenaneacsu.bookfolio.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.elenaneacsu.bookfolio.extensions.Result as BookfolioResult

@HiltViewModel
class AuthViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val authRepository: AuthRepository
) : BaseViewModel(resourceString, coroutineContextProvider) {

    val email = MutableLiveData("test@test.com")
    val password = MutableLiveData("123456")
    val name = MutableLiveData("Test Name")

    private val _loginResult = MutableLiveData<BookfolioResult<String>>()
    val loginResult: LiveData<BookfolioResult<String>>
        get() = _loginResult

    private val _signupResult = MutableLiveData<BookfolioResult<String>>()
    val signupResult: LiveData<BookfolioResult<String>>
        get() = _signupResult

    private val _isUserLoggedIn = MutableLiveData<Boolean>()


    fun login() = makeRequest(resourceString, ioContext, _loginResult) {
        handleFieldsValidation(isLoggingIn = true)

        if (isRequestOk) {
            _loginResult.postValue(BookfolioResult.loading())
            authRepository.login(email.value!!, password.value!!)
            _loginResult.postValue(BookfolioResult.success())
        } else {
            setError(resourceString.getString(R.string.check_credentials))
        }
    }

    fun signup() = makeRequest(resourceString, ioContext, _signupResult) {
        handleFieldsValidation(isLoggingIn = false)

        if (isRequestOk) {
            _signupResult.postValue(BookfolioResult.loading())
            authRepository.signup(name.value!!, email.value!!, password.value!!)
            _signupResult.postValue(BookfolioResult.success())
        } else {
            setError(resourceString.getString(R.string.check_credentials))
        }
    }

    private fun handleFieldsValidation(isLoggingIn: Boolean) {
        var isNameOk = false
        var isEmailOk = false
        var isPasswordOk = false

        name.checkStringValue(
            condition = { it.isValidName() },
            noError = {
                isNameOk = true
            },
            hasError = {
                isNameOk = false
            },
            isNullOrEmpty = {
                isNameOk = false
            }
        )

        email.checkStringValue(
            condition = { it.isValidEmail() },
            noError = {
                isEmailOk = true
            },
            hasError = {
                isEmailOk = false
            },
            isNullOrEmpty = {
                isEmailOk = false
            }
        )

        password.checkStringValue(
            condition = { it.isValidPassword() },
            noError = {
                isPasswordOk = true
            },
            hasError = {
                isPasswordOk = false
            },
            isNullOrEmpty = {
                isPasswordOk = false
            }
        )

        isRequestOk =
            if (isLoggingIn) isEmailOk && isPasswordOk else isNameOk && isEmailOk && isPasswordOk

    }

}