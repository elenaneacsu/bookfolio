package com.elenaneacsu.bookfolio.ui.shelves

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentShelvesBinding
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.getThemeColor
import com.elenaneacsu.bookfolio.extensions.toast
import com.elenaneacsu.bookfolio.extensions.updateStatusBarColor
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.ui.MainActivity
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        (activity as? MainActivity)?.apply {
            updateStatusBarColor(R.color.primary, false)
            manageBottomNavigationVisibility(View.VISIBLE)
        }

        return view
    }

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
            viewModel.getShelvesScreenInfo()
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

        viewModel.getShelvesScreenInfo()
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.shelvesResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { pair ->
                        shelvesAdapter?.add(pair.first)
                        shelvesAdapter?.notifyDataSetChanged()

                        currentlyReadingAdapter?.add(pair.second)
                        currentlyReadingAdapter?.notifyDataSetChanged()
                    }
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

    override fun onBookClicked(book: BookDetailsMapper) {
        val direction = ShelvesFragmentDirections.actionShelvesFragmentToBookDetails(
            book,
            viewModel.currentlyReadingShelf
        )
        findNavController().navigate(direction)
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

}