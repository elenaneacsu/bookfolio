package com.elenaneacsu.bookfolio.ui.auth

import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() : BaseRepository() {

    private var auth: FirebaseAuth = Firebase.auth

    suspend fun login(email: String, password: String): AuthResult? =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun signup(email: String, password: String): AuthResult? =
        auth.createUserWithEmailAndPassword(email, password).await()


}