package com.elenaneacsu.bookfolio.ui.search.book_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentBookDetailsBinding
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment

/**
 * Created by Elena Neacsu on 01/06/21
 */
class BookDetailsFragment : BaseMvvmFragment<BookDetailsViewModel, FragmentBookDetailsBinding>(
    R.layout.fragment_book_details, BookDetailsViewModel::class.java
) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false)
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