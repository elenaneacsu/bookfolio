package com.elenaneacsu.bookfolio.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentSearchBinding
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.getThemeColor
import com.elenaneacsu.bookfolio.extensions.hideKeyboard
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 17/05/21
 */
@AndroidEntryPoint
class SearchFragment : BookAdapter.OnItemClickListener,
    BaseMvvmFragment<SearchViewModel, FragmentSearchBinding>(
        R.layout.fragment_search, SearchViewModel::class.java
    ) {

    private val searchMenu: Menu
        get() = viewBinding.toolbar.menu
    private lateinit var searchMenuItem: MenuItem
    private lateinit var searchView: SearchView
    private var booksAdapter: BookAdapter? = null

    private var searchQueryTextListener: SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                submitQueryText(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        setHasOptionsMenu(true)

        return view
    }

    override fun initViews() {
        super.initViews()

        searchMenuItem = searchMenu.findItem(R.id.search)
        searchView = searchMenuItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(searchQueryTextListener)
            maxWidth = Int.MAX_VALUE
        }

        activity?.let {
            viewBinding.pullToRefresh.setColorSchemeColors(it.getThemeColor(R.attr.colorAccent))
        }
        viewBinding.pullToRefresh.setOnRefreshListener {
            hideProgress()
        }

        context?.let {
            booksAdapter = BookAdapter(it, this@SearchFragment)
        }

        viewBinding.booksRecyclerView.adapter = booksAdapter

    }

    override fun initObservers() {
        super.initObservers()

        viewModel.booksSearchResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { book -> booksAdapter?.add(book) }
                    booksAdapter?.notifyDataSetChanged()
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })

        viewModel.bookDetailsResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { bookDetails ->
                        if (viewModel.isBookClickHandled.value == false) {
                            val direction = SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment(bookDetails)
                            findNavController().navigate(direction)
                        }
                    }
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })
    }


    override fun onBookClicked(book: Item) {
        book.id?.let { viewModel.getBookDetails(it) }
    }

    override fun hideProgress() {
        viewBinding.pullToRefresh.isRefreshing = false
    }

    override fun showProgress() {
        viewBinding.pullToRefresh.isRefreshing = true
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }

    // region Private helpers

    private fun submitQueryText(query: String?) {
        (activity as? AppCompatActivity)?.hideKeyboard(searchView)
        searchView.clearFocus()
        query?.let {
            viewModel.searchBooks(it)
        }
    }

    // endregion
}