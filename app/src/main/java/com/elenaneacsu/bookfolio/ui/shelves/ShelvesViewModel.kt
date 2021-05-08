package com.elenaneacsu.bookfolio.ui.shelves

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

}