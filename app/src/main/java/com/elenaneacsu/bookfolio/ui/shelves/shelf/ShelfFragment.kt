package com.elenaneacsu.bookfolio.ui.shelves.shelf

import android.os.Bundle
import android.view.View
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentShelfBinding
import com.elenaneacsu.bookfolio.models.google_books_api_models.ImageLinks
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.models.google_books_api_models.VolumeInfo
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments ?: return

        val args = ShelfFragmentArgs.fromBundle(bundle)
        args.shelf?.let { viewBinding.toolbar.title = it.name }
    }

    override fun initViews() {
        super.initViews()

        context?.let {
            booksAdapter = BookAdapter(it, this@ShelfFragment)
        }

        viewBinding.booksRecyclerView.adapter = booksAdapter
        mockData()
    }

    override fun hideProgress() {
//        viewBinding.pullToRefresh.isRefreshing = false
    }

    override fun showProgress() {
//        viewBinding.pullToRefresh.isRefreshing = true
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }

    override fun onBookClicked(book: Item) {

    }

    private fun mockData() {
        val book1 = Item(volumeInfo = VolumeInfo("What Matters Most Is How Well You Walk Through The Fire",
        authors = listOf("Charles Bukowski"), imageLinks = ImageLinks("http://books.google.com/books/content?id=eFrnwPziy24C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            "http://books.google.com/books/content?id=eFrnwPziy24C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api")))

        val book2 = Item(volumeInfo = VolumeInfo("Vino cu mine stiu exact unde mergem",
            authors = listOf("Dan Sociu"), imageLinks = ImageLinks("http://books.google.com/books/content?id=vyyTDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
                "http://books.google.com/books/content?id=vyyTDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api")))

        val book3 = Item(volumeInfo = VolumeInfo("Soldatii: poveste din Ferentari", authors = listOf("Adrian Schiop"),
        imageLinks = ImageLinks("http://books.google.com/books/content?id=7ymTDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api")
        ))

        val book4 = Item(volumeInfo = VolumeInfo("Love", authors = listOf("Stendhal"),
            imageLinks = ImageLinks("http://books.google.com/books/content?id=ungt2wstvLQC&printsec=frontcover&img=1&zoom=5&source=gbs_api")
        ))

        val book5 = Item(volumeInfo = VolumeInfo("Giovanni's Room", authors = listOf("James Baldwin"),
            imageLinks = ImageLinks("http://books.google.com/books/content?id=PJgrAQAAIAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api")
        ))

        val books = listOf(book1, book2, book3, book4, book5)
        booksAdapter?.add(books)
        booksAdapter?.notifyDataSetChanged()
    }
}