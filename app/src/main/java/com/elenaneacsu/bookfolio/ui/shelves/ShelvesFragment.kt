package com.elenaneacsu.bookfolio.ui.shelves

import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentShelvesBinding
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 07/05/21
 */
@AndroidEntryPoint
class ShelvesFragment : BaseMvvmFragment<ShelvesViewModel, FragmentShelvesBinding>(
    R.layout.fragment_shelves, ShelvesViewModel::class.java
) {

    override fun initViewModel() {
        super.initViewModel()
        viewBinding.viewModel = viewModel
    }

    override fun initViews() {
        super.initViews()
    }

    override fun initObservers() {
        super.initObservers()
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

    override fun showProgress() {
        TODO("Not yet implemented")
    }

    override fun errorAlert(message: String) {
        TODO("Not yet implemented")
    }

    override fun successAlert(message: String) {
        TODO("Not yet implemented")
    }
}