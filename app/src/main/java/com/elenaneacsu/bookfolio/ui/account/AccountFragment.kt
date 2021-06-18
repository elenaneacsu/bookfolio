package com.elenaneacsu.bookfolio.ui.account

import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentAccountBinding
import com.elenaneacsu.bookfolio.extensions.startActivityWithFlags
import com.elenaneacsu.bookfolio.ui.auth.AuthActivity
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 08/05/21
 */
@AndroidEntryPoint
class AccountFragment : BaseMvvmFragment<AccountViewModel, FragmentAccountBinding>(
    R.layout.fragment_account, AccountViewModel::class.java
) {

    override fun initViews() {
        super.initViews()

        viewBinding.logOut.setOnClickListener {
            viewModel.signOut()
            activity?.startActivityWithFlags(AuthActivity::class.java)
        }
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