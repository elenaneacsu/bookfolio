package com.elenaneacsu.bookfolio.ui.shelves

import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 08/05/21
 */
class ShelvesRepository @Inject constructor() : BaseRepository() {

    fun getUserName() = auth.currentUser?.displayName ?: "empty"

}