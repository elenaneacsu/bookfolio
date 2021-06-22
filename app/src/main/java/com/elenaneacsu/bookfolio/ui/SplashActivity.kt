package com.elenaneacsu.bookfolio.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.extensions.updateStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 18/06/21
 */
@AndroidEntryPoint
class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        updateStatusBarColor(R.color.accent, false)
    }
}