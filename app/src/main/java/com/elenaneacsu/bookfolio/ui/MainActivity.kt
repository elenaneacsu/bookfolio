package com.elenaneacsu.bookfolio.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.elenaneacsu.bookfolio.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.shelves -> {
//            openShelvesFragment()
            findNavController(R.id.fragment_nav_host).navigate(R.id.shelvesFragment)
            true
        }
        R.id.quotes -> {
//            openQuotesFragment()
            true
        }
        R.id.discover -> {
//            openDiscoverFragment()
            findNavController(R.id.fragment_nav_host).navigate(R.id.searchFragment)
            true
        }
        R.id.account -> {
//            openAccountFragment()
            findNavController(R.id.fragment_nav_host).navigate(R.id.accountFragment)
            true
        }
        else -> false
    }

    override fun onNavigationItemReselected(item: MenuItem) {
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment_nav_host).navigateUp()
    }
}