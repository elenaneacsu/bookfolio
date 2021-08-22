package com.elenaneacsu.bookfolio.ui.shelves.book_journal

import com.elenaneacsu.bookfolio.ui.search.book_details.BookDetailsRepository
import com.elenaneacsu.bookfolio.utils.ResourceString
import com.elenaneacsu.bookfolio.viewmodel.BaseViewModel
import com.elenaneacsu.bookfolio.viewmodel.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 22/08/2021
 */
@HiltViewModel
class JournalViewModel @Inject constructor(
    resourceString: ResourceString,
    coroutineContextProvider: CoroutineContextProvider,
    private val repository: BookDetailsRepository
) : BaseViewModel(
    resourceString, coroutineContextProvider
) {
}