package com.elenaneacsu.bookfolio.ui.shelves

import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentShelvesBinding
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.getThemeColor
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.google_books_api_models.ImageLinks
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.models.google_books_api_models.VolumeInfo
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Elena Neacsu on 07/05/21
 */
@AndroidEntryPoint
class ShelvesFragment : ShelfAdapter.OnItemClickListener,
    CurrentlyReadingBookAdapter.OnItemClickListener,
    BaseMvvmFragment<ShelvesViewModel, FragmentShelvesBinding>(
        R.layout.fragment_shelves, ShelvesViewModel::class.java
    ) {
    private var shelvesAdapter: ShelfAdapter? = null
    private var currentlyReadingAdapter: CurrentlyReadingBookAdapter? = null

    override fun initViewModel() {
        super.initViewModel()
        viewBinding.viewModel = viewModel
    }

    override fun initViews() {
        super.initViews()

        activity?.let {
            viewBinding.pullToRefresh.setColorSchemeColors(it.getThemeColor(R.attr.colorAccent))
        }
        viewBinding.pullToRefresh.setOnRefreshListener {
            viewModel.getShelves()
        }

        context?.let {
            shelvesAdapter = ShelfAdapter(it, this@ShelvesFragment)
            currentlyReadingAdapter = CurrentlyReadingBookAdapter(it, this@ShelvesFragment)
        }

        viewBinding.shelvesRecyclerView.adapter = shelvesAdapter

        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()

        viewBinding.currentlyReadingViewpager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = currentlyReadingAdapter
            offscreenPageLimit = 3

            setPageTransformer { page, position ->
                val myOffset: Float = position * -(2 * pageOffset + pageMargin)
                if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -myOffset
                    } else {
                        page.translationX = myOffset
                    }
                } else {
                    page.translationY = myOffset
                }
            }
        }
        mockData()

        viewModel.getShelves()
//        viewModel.getCurrentlyReading()

    }

    override fun initObservers() {
        super.initObservers()

        viewModel.mla.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { it1 -> shelvesAdapter?.add(it1) }
                    shelvesAdapter?.notifyDataSetChanged()
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })
    }

    override fun onShelfClicked(shelf: Shelf) {
        val direction = ShelvesFragmentDirections.actionShelvesFragmentToShelfFragment(shelf)
        findNavController().navigate(direction)
    }

    override fun onBookClicked(book: Item) {
        TODO("Not yet implemented")
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

    private fun mockData() {
        val book1 = Item(
            volumeInfo = VolumeInfo(
                title = "You Get So Alone At Times", authors = listOf("Charles Bukowski"),
                imageLinks = ImageLinks("http://books.google.com/books/content?id=j_ktEg3xESoC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api")
            )
        )

        val book2 = Item(
            volumeInfo = VolumeInfo(
                title = "A Streetcar Named Desire", authors = listOf("Tennessee Williams"),
                imageLinks = ImageLinks("http://books.google.com/books/content?id=-qO2F_suXzwC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api")
            )
        )
        currentlyReadingAdapter?.add(listOf(book1, book2))
        currentlyReadingAdapter?.notifyDataSetChanged()
    }

}