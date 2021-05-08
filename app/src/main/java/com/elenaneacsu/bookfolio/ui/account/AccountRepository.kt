package com.elenaneacsu.bookfolio.ui.account

import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 08/05/21
 */
class AccountRepository @Inject constructor() : BaseRepository() {

    fun signOut() = auth.signOut()
}