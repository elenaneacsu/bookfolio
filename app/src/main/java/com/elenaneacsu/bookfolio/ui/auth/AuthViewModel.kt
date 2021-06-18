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

//    val email = MutableLiveData("test@test.com")
    val email = MutableLiveData<String>()

//    val password = MutableLiveData("123456")
    val password = MutableLiveData<String>()

//    val name = MutableLiveData("Test Name")
    val name = MutableLiveData<String>()

    private val _loginResult = MutableLiveData<BookfolioResult<String>>()
    val loginResult: LiveData<BookfolioResult<String>>
        get() = _loginResult

    private val _signupResult = MutableLiveData<BookfolioResult<String>>()
    val signupResult: LiveData<BookfolioResult<String>>
        get() = _signupResult

    private val _forgotPassResult = MutableLiveData<BookfolioResult<Void>>()
    val forgotPassResult: LiveData<BookfolioResult<Void>>
        get() = _forgotPassResult

    private val _isForgotPassHandled = MutableLiveData<Boolean>()
    val isForgotPassHandled: LiveData<Boolean>
        get() = _isForgotPassHandled


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
            authRepository.signup(
                name.value!!,
                email.value!!,
                password.value!!
            )
            _signupResult.postValue(BookfolioResult.success())
        } else {
            setError(resourceString.getString(R.string.check_credentials))
        }
    }

    fun forgotPassword() = makeRequest(resourceString, ioContext, _forgotPassResult) {
        handleFieldsValidation(isLoggingIn = false, hasForgotPassword = true)
        if (isRequestOk) {
            _forgotPassResult.postValue(BookfolioResult.loading())

            _isForgotPassHandled.postValue(false)

            authRepository.forgotPassword(email.value!!)
            _forgotPassResult.postValue(BookfolioResult.success())

            _isForgotPassHandled.postValue(true)
        } else {
            setError(resourceString.getString(R.string.invalid_email))
        }
    }

    private fun handleFieldsValidation(isLoggingIn: Boolean, hasForgotPassword: Boolean = false) {
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
            when {
                isLoggingIn -> isEmailOk && isPasswordOk
                hasForgotPassword -> isEmailOk
                else -> isNameOk && isEmailOk && isPasswordOk
            }

    }

}