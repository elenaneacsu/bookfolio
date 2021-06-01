package com.elenaneacsu.bookfolio.ui.shelves

import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentShelvesBinding
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.getThemeColor
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 07/05/21
 */
@AndroidEntryPoint
class ShelvesFragment : ShelfAdapter.OnItemClickListener,
    BaseMvvmFragment<ShelvesViewModel, FragmentShelvesBinding>(
        R.layout.fragment_shelves, ShelvesViewModel::class.java
    ) {
    private var shelvesAdapter: ShelfAdapter? = null

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
        }

        viewBinding.shelvesRecyclerView.adapter = shelvesAdapter

        viewModel.getShelves()
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

}