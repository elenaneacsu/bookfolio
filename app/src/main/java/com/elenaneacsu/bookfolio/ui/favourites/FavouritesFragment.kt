package com.elenaneacsu.bookfolio.ui.favourites

import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentFavouritesBinding
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 13/06/21
 */
@AndroidEntryPoint
class FavouritesFragment: BaseMvvmFragment<FavouritesViewModel, FragmentFavouritesBinding>(
    R.layout.fragment_favourites, FavouritesViewModel::class.java
) {

    override fun hideProgress() {
    }

    override fun showProgress() {
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }
}