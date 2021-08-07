package com.elenaneacsu.bookfolio.viewmodel

import com.elenaneacsu.bookfolio.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseRepository : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val delay = async(Dispatchers.IO) { delay(1000) }

    fun cleanup() {
        job.cancel()
    }

    fun getMainDocumentOfRegisteredUser() = auth.currentUser?.uid?.let {
        firestore.collection(Constants.USERS_COLLECTION).document(it)
    }
}