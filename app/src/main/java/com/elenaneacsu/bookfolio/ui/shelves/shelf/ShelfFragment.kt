package com.elenaneacsu.bookfolio.ui.shelves.shelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentShelfBinding
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.google_books_api_models.ImageLinks
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.models.google_books_api_models.VolumeInfo
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.ui.search.BookAdapter
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 15/05/21
 */
@AndroidEntryPoint
class ShelfFragment : BookAdapter.OnItemClickListener,
    BaseMvvmFragment<ShelfViewModel, FragmentShelfBinding>(
        R.layout.fragment_shelf, ShelfViewModel::class.java
    ) {
    private var booksAdapter: BookAdapter? = null
    private var shelf: Shelf? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        (activity as? MainActivity)?.apply {
            updateStatusBarColor(R.color.primary, false)
            manageBottomNavigationVisibility(View.VISIBLE)
            viewBinding.pullToRefresh.setColorSchemeColors(getThemeColor(R.attr.colorAccent))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments ?: return

        val args = ShelfFragmentArgs.fromBundle(bundle)
        shelf = args.shelf?.also {
            viewBinding.toolbar.title = it.name
            viewModel.getBooks(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initViews() {
        super.initViews()

        context?.let {
            booksAdapter = BookAdapter(it, this@ShelfFragment)
        }

        viewBinding.apply {
            booksRecyclerView.adapter = booksAdapter
            toolbar.setNavigationOnClickListener {
                (activity as? MainActivity)?.onSupportNavigateUp()
            }
            shelf?.let {
                pullToRefresh.setOnRefreshListener {
                    viewModel.getBooks(it)
                }
            } ?: pullToRefresh.setRefreshing(false)
        }
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.booksInShelfResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    viewBinding.booksRecyclerView.visibility = View.VISIBLE
                    it.data?.let { books ->
                        if (books.isNotEmpty()) {
                            showEmptyShelfViews(shouldDisplay = false)
                            booksAdapter?.add(books)
                            booksAdapter?.notifyDataSetChanged()
                        } else {
                            showEmptyShelfViews(shouldDisplay = true)
                        }
                    }
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })

        viewModel.removeBookResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    activity?.getString(R.string.removed_book_success)?.let { message ->
                        successAlert(
                            message
                        )
                    }
                    it.data?.let { book -> booksAdapter?.onBookRemoved(book) }
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    it.message ?: activity?.getString(R.string.default_error_message)
                        ?.let { message ->
                            errorAlert(
                                message
                            )
                        }
                }
            }
        })
    }

    override fun hideProgress() {
        viewBinding.apply {
            pullToRefresh.isRefreshing = false
            viewOverlay.visibility = View.GONE
        }
    }

    override fun showProgress() {
        viewBinding.apply {
            pullToRefresh.isRefreshing = true
            viewOverlay.visibility = View.VISIBLE
        }
    }

    override fun errorAlert(message: String) {
        toast(message)
    }

    override fun successAlert(message: String) {
        toast(message)
    }

    override fun onBookClicked(book: BookDetailsMapper) {
        val direction = ShelfFragmentDirections.actionShelfFragmentToBookDetails(book, shelf)
        findNavController().navigate(direction)
    }

    override fun onRemoveBook(book: BookDetailsMapper) {
        context?.let { ctx ->
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setTitle("Remove book")
                setMessage("Are you sure you want to remove this book from your ${shelf?.name} shelf?")
                positiveButton {
                    shelf?.let { viewModel.removeBook(book, it) }
                }
                negativeButton("Cancel") {
                    it.dismiss()
                }
            }
        }
    }

    private fun showEmptyShelfViews(shouldDisplay: Boolean) {
        val emptyShelfViewsVisibility = if (shouldDisplay) View.VISIBLE else View.GONE
        val recyclerViewVisibility = if (!shouldDisplay) View.VISIBLE else View.GONE

        viewBinding.apply {
            booksPlaceholderImage.visibility = emptyShelfViewsVisibility
            booksPlaceholderText.visibility = emptyShelfViewsVisibility
            booksRecyclerView.visibility = recyclerViewVisibility
        }
    }

    private fun mockData() {
        val book1 = Item(
            volumeInfo = VolumeInfo(
                "What Matters Most Is How Well You Walk Through The Fire",
                authors = listOf("Charles Bukowski"), imageLinks = ImageLinks(
                    "http://books.google.com/books/content?id=eFrnwPziy24C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
                    "http://books.google.com/books/content?id=eFrnwPziy24C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"
                )
            )
        )

        val book2 = Item(
            volumeInfo = VolumeInfo(
                "Vino cu mine stiu exact unde mergem",
                authors = listOf("Dan Sociu"), imageLinks = ImageLinks(
                    "http://books.google.com/books/content?id=vyyTDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
                    "http://books.google.com/books/content?id=vyyTDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"
                )
            )
        )

        val book3 = Item(
            volumeInfo = VolumeInfo(
                "Soldatii: poveste din Ferentari", authors = listOf("Adrian Schiop"),
                imageLinks = ImageLinks("http://books.google.com/books/content?id=7ymTDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api")
            )
        )

        val book4 = Item(
            volumeInfo = VolumeInfo(
                "Love", authors = listOf("Stendhal"),
                imageLinks = ImageLinks("http://books.google.com/books/content?id=ungt2wstvLQC&printsec=frontcover&img=1&zoom=5&source=gbs_api")
            )
        )

        val book5 = Item(
            volumeInfo = VolumeInfo(
                "Giovanni's Room", authors = listOf("James Baldwin"),
                imageLinks = ImageLinks("http://books.google.com/books/content?id=PJgrAQAAIAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api")
            )
        )

        val books = listOf(book1, book2, book3, book4, book5)
//        booksAdapter?.add(books)
//        booksAdapter?.notifyDataSetChanged()
    }
}