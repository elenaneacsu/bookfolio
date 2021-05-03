package com.elenaneacsu.bookfolio.ui.auth.login

import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentLoginBinding
import com.elenaneacsu.bookfolio.extensions.logDebug
import com.elenaneacsu.bookfolio.extensions.toast
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseMvvmFragment<LoginViewModel, FragmentLoginBinding>(
    R.layout.fragment_login, LoginViewModel::class.java
) {

    override fun initViewModel() {
        super.initViewModel()
        viewBinding.viewModel = viewModel
    }

    override fun initViews() {
        super.initViews()
//        viewBinding.textViewRegisterRedirect.apply {
//            showUnderline()
//            setOnOneOffClickListener {
//                findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToNavigationRegister())
//            }
//        }
        viewBinding.login.setOnOneOffClickListener {
            viewModel.login()
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.loginResult.observe(viewLifecycleOwner, {
            toast(it)
//            when (it.status) {
//                Result.Status.LOADING -> showProgress()
//                Result.Status.SUCCESS -> {
//                    hideProgress()
//                    activity?.startActivityWithFlags(MainActivity::class.java)
//                }
//                Result.Status.ERROR -> {
//                    hideProgress()
//                    errorAlert(it.message ?: getString(R.string.default_error_message))
//                }
//            }
        })
    }

    override fun hideProgress() {
    }

    override fun showProgress() {
    }

    override fun errorAlert(message: String) {
        logDebug(message)
        toast(message)
    }

    override fun successAlert(message: String) {
        toast(message)
    }
}