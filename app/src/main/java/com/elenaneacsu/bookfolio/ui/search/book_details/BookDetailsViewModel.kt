package com.elenaneacsu.bookfolio.ui.search.book_details

import com.elenaneacsu.bookfolio.ui.search.SearchRepository
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 01/06/21
 */
class BookDetailsViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val repository: SearchRepository
) : BaseViewModel(
    resourceString, coroutineContextProvider) {

}