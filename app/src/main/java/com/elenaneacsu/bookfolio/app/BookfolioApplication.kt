package com.elenaneacsu.bookfolio.app

import android.app.Application
import com.elenaneacsu.bookfolio.extensions.startActivityWithFlags
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Elena Neacsu on 01/05/21
 */
@HiltAndroidApp
class BookfolioApplication : Application() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()

        auth = Firebase.auth
        if (auth.currentUser != null)
            startActivityWithFlags(MainActivity::class.java)
    }
}