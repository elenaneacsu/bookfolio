package com.elenaneacsu.bookfolio.ui.shelves.shelf

import android.os.Bundle
import android.view.View
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentShelfBinding
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 15/05/21
 */
@AndroidEntryPoint
class ShelfFragment : BaseMvvmFragment<ShelfViewModel, FragmentShelfBinding>(
    R.layout.fragment_shelf, ShelfViewModel::class.java
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments ?: return

        val args = ShelfFragmentArgs.fromBundle(bundle)
        args.shelf?.let { viewBinding.toolbar.title = it.name }
    }

    override fun initViews() {
        super.initViews()
        viewBinding.testButton.setOnClickListener {
            viewModel.makeTestRequest("bukowski")
        }
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
}