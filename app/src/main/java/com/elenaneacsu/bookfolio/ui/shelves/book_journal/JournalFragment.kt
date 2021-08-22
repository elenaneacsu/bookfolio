package com.elenaneacsu.bookfolio.ui.shelves.book_journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentFavouritesBinding
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.ui.favourites.QuotesAdapter
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JournalFragment : BaseMvvmFragment<JournalViewModel, FragmentFavouritesBinding>(
    R.layout.fragment_favourites, JournalViewModel::class.java
) {

    private var quotesAdapter: QuotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        (activity as? MainActivity)?.apply {
            updateStatusBarColor(R.color.primary, false)
            viewBinding.pullToRefresh.setColorSchemeColors(getThemeColor(R.attr.colorAccent))
        }
        return view
    }

    override fun initViews() {
        super.initViews()

        context?.let {
            quotesAdapter = QuotesAdapter(it)
        }

        viewBinding.apply {

            toolbar.apply {
                setNavigationOnClickListener {
                    (activity as? MainActivity)?.onSupportNavigateUp()
                }
                title = activity?.getString(R.string.book_journal_title)
            }

            quotesRecyclerView.adapter = quotesAdapter

            temporaryFab.setOnOneOffClickListener {
                showDialogToAddQuote()
            }
        }
    }

    override fun hideProgress() {

    }

    override fun showProgress() {
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }

    private fun showDialogToAddQuote() {
        context?.let { ctx ->
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setView(R.layout.add_quote_dialog_layout)

                positiveButton("Save") {

                }

                negativeButton("Cancel") {

                }
            }
        }
    }
}