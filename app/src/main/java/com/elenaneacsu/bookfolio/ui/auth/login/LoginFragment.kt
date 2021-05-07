package com.elenaneacsu.bookfolio.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.elenaneacsu.bookfolio.MainActivity
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentLoginBinding
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.ui.auth.AuthActivity
import com.elenaneacsu.bookfolio.ui.auth.AuthViewModel
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint
import com.elenaneacsu.bookfolio.extensions.Result as BookfolioResult


@AndroidEntryPoint
class LoginFragment : BaseMvvmFragment<AuthViewModel, FragmentLoginBinding>(
    R.layout.fragment_login, AuthViewModel::class.java
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        (activity as? AuthActivity)?.updateStatusBarColor(R.color.white_grey, true)
        return view
    }

    override fun initViewModel() {
        super.initViewModel()
        viewBinding.viewModel = viewModel
    }

    override fun initViews() {
        super.initViews()

        viewBinding.apply {

            login.setOnClickListener {
                this@LoginFragment.viewModel.login()
            }

            goToSignUp.setOnOneOffClickListener {
                goToSignUp()
            }

            email.setOnDrawableRightClickListener()
        }

    }

    override fun initObservers() {
        super.initObservers()
        viewModel.loginResult.observe(viewLifecycleOwner, {
            when (it.status) {
                BookfolioResult.Status.LOADING -> showProgress()
                BookfolioResult.Status.SUCCESS -> {
                    hideProgress()
                    activity?.startActivityWithFlags(MainActivity::class.java)
                }
                BookfolioResult.Status.ERROR -> {
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
        logDebug(message)
        toast(message)
    }

    override fun successAlert(message: String) {
        toast(message)
    }

    private fun goToSignUp() {
        Navigation.findNavController(viewBinding.goToSignUp)
            .navigate(R.id.action_loginFragment_to_signupFragment)
    }
}