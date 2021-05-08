package com.elenaneacsu.bookfolio.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.elenaneacsu.bookfolio.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun onNavigateUp(): Boolean {
        findNavController(R.id.fragment_nav_host).navigateUp()
        return super.onNavigateUp()
    }
}