package com.elenaneacsu.bookfolio.ui.auth.login

import com.elenaneacsu.bookfolio.vm.BaseRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor() : BaseRepository() {

    private var auth: FirebaseAuth = Firebase.auth

    suspend fun login(email: String, password: String): AuthResult? =
        auth.signInWithEmailAndPassword(email, password).await()

}