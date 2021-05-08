package com.elenaneacsu.bookfolio.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.elenaneacsu.bookfolio.extensions.Result as BookfolioResult

/**
 * Created by Elena Neacsu on 08/05/21
 */
@HiltViewModel
class AccountViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val accountRepository: AccountRepository
) : BaseViewModel(resourceString, coroutineContextProvider) {

    private val _signOutResult = MutableLiveData<BookfolioResult<Boolean>>()
    val signOutResult: LiveData<BookfolioResult<Boolean>>
        get() = _signOutResult

    fun signOut() {
        accountRepository.signOut()
    }
}