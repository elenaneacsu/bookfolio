package com.elenaneacsu.bookfolio.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.vm.BaseViewModel
import com.elenaneacsu.bookfolio.vm.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Grigore Cristian-Andrei on 15.03.2021.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val loginRepository: LoginRepository
) : BaseViewModel(resourceString, coroutineContextProvider) {

    val email = MutableLiveData("test@test.com")
    val password = MutableLiveData("123456")

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String>
        get() = _loginResult

    fun login() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = loginRepository.login(email.value!!, password.value!!)
            _loginResult.postValue(response?.user?.email ?: "empty")
        } catch (e: Exception) {
            _loginResult.postValue("error")
        }

    }

//    fun login() = makeRequest(resourceString, ioContext, _loginResult) {
//        val loginRequestModel = LoginRequestModel()
//
//        handleFieldsValidation(loginRequestModel)
//
//        if (isRequestOk) {
//            _loginResult.postValue(Result.loading())
//            _loginResult.postValue(Result.success(loginRepository.login(loginRequestModel).data))
//        } else {
//            setError("please check your fields")
//        }
//    }
//
//    private fun handleFieldsValidation(loginRequestModel: LoginRequestModel) {
//        var isEmailOk = false
//        var isPasswordOk = false
//        email.checkStringValue(
//            condition = { it.isValidEmail() },
//            noError = {
//                loginRequestModel.email = it
//                isEmailOk = true
//            },
//            hasError = {
//                isEmailOk = false
//            },
//            isNullOrEmpty = {
//                isEmailOk = false
//            }
//        )
//
//        password.checkStringValue(
//            condition = { it.isValidPassword() },
//            noError = {
//                isPasswordOk = true
//                loginRequestModel.password = it
//            },
//            hasError = {
//                isPasswordOk = false
//            },
//            isNullOrEmpty = {
//                isPasswordOk = false
//            }
//        )
//
//        isRequestOk = isEmailOk && isPasswordOk
//
//    }

}