package com.elenaneacsu.bookfolio.ui.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentSignupBinding
import com.elenaneacsu.bookfolio.extensions.toast
import com.elenaneacsu.bookfolio.extensions.updateStatusBarColor
import com.elenaneacsu.bookfolio.ui.auth.AuthActivity
import com.elenaneacsu.bookfolio.ui.auth.login.LoginViewModel
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseMvvmFragment<LoginViewModel, FragmentSignupBinding>(
    R.layout.fragment_signup, LoginViewModel::class.java
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val toolbar = viewBinding.toolbar

        (activity as? AuthActivity)?.apply {
            setSupportActionBar(toolbar)
            updateStatusBarColor(R.color.primary, false)
        }

        return view
    }

    override fun initViewModel() {
        super.initViewModel()
        viewBinding.viewModel = viewModel
    }

    override fun initViews() {
        super.initViews()

        // set on click listeners
        viewBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.signupResult.observe(viewLifecycleOwner, {
            toast(it)
        })
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