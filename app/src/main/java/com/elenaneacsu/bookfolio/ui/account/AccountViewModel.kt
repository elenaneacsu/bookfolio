package com.elenaneacsu.bookfolio.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.makeRequest
import com.elenaneacsu.bookfolio.models.User
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import com.elenaneacsu.bookfolio.viewmodel.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 08/05/21
 */
@HiltViewModel
class AccountViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val accountRepository: AccountRepository
) : BaseViewModel(resourceString, coroutineContextProvider) {

    private val _signOutResult = MutableLiveData<Result<Boolean>>()
    val signOutResult: LiveData<Result<Boolean>>
        get() = _signOutResult

    private val _userDataResult = SingleLiveEvent<Result<User>>()
    val userDataResult: LiveData<Result<User>>
        get() = _userDataResult

    private lateinit var userData: User
    fun signOut() {
        accountRepository.signOut()
    }

    fun getUserDetails() = makeRequest(resourceString, ioContext, _userDataResult) {
        userData = accountRepository.getUser()
        _userDataResult.postValue(Result.success(userData))
    }

    fun updateUserName(name: String?) = makeRequest(resourceString, ioContext, _userDataResult) {
        if (name?.isNullOrEmpty() == true) {
            setError(resourceString.getString(R.string.default_error_message))
        } else {
            accountRepository.updateName(name)
            userData.name = name
            _userDataResult.postValue(Result.success(userData))
        }
    }

    fun getName() = userData.name
}