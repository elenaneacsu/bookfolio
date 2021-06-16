package com.elenaneacsu.bookfolio.ui.search.book_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentBookDetailsBinding
import com.elenaneacsu.bookfolio.extensions.toast
import com.elenaneacsu.bookfolio.ui.shelves.shelf.ShelfFragmentArgs
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 01/06/21
 */
@AndroidEntryPoint
class BookDetailsFragment : BaseMvvmFragment<BookDetailsViewModel, FragmentBookDetailsBinding>(
    R.layout.fragment_book_details, BookDetailsViewModel::class.java
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments ?: return

        val args = BookDetailsFragmentArgs.fromBundle(bundle)
        toast(args.book.volumeInfo?.title ?: "no title was received", Toast.LENGTH_LONG)

    }

    override fun hideProgress() {
    }

    override fun showProgress() {
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }
}