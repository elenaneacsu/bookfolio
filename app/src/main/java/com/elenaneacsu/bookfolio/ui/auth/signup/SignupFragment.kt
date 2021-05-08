package com.elenaneacsu.bookfolio.ui.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentSignupBinding
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.setOnDrawableRightClickListener
import com.elenaneacsu.bookfolio.extensions.startActivityWithFlags
import com.elenaneacsu.bookfolio.extensions.updateStatusBarColor
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.ui.auth.AuthActivity
import com.elenaneacsu.bookfolio.ui.auth.AuthViewModel
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseMvvmFragment<AuthViewModel, FragmentSignupBinding>(
    R.layout.fragment_signup, AuthViewModel::class.java
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
        viewBinding.apply {

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            name.setOnDrawableRightClickListener()

            email.setOnDrawableRightClickListener()

            signup.setOnClickListener {
                this@SignupFragment.viewModel.signup()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.signupResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    activity?.startActivityWithFlags(MainActivity::class.java)
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })
    }

    override fun hideProgress() {
        viewBinding.progressBar.visibility = View.GONE
    }

    override fun showProgress() {
        viewBinding.progressBar.visibility = View.VISIBLE
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }
}