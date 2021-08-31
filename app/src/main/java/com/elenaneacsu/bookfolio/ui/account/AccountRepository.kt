package com.elenaneacsu.bookfolio.ui.account

import com.elenaneacsu.bookfolio.models.User
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 08/05/21
 */
class AccountRepository @Inject constructor() : BaseRepository() {

    fun signOut() = auth.signOut()

    fun getEmail() = auth.currentUser?.email ?: ""

    suspend fun getName(): String {
        val nameTask = getMainDocumentOfRegisteredUser()?.get()?.await()

        return nameTask?.get("name")?.toString() ?: ""
    }

    suspend fun getUser(): User {
        val email = getEmail()
        val name = getName()

        return User(name, email)
    }

    suspend fun updateName(name: String) {
        getMainDocumentOfRegisteredUser()?.update("name", name)?.await()
    }
}