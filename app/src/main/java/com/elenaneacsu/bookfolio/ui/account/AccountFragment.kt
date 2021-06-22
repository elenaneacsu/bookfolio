package com.elenaneacsu.bookfolio.ui.account

import androidx.appcompat.app.AppCompatActivity
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentAccountBinding
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.ui.SplashActivity
import com.elenaneacsu.bookfolio.ui.auth.AuthActivity
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
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
            showLogOutDialog()
        }

        viewBinding.deleteAccount.setOnOneOffClickListener {
            (activity as? AppCompatActivity)?.startActivityNormal(SplashActivity::class.java)
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

    private fun showLogOutDialog() {
        context?.let { ctx ->
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setTitle("Log out")
                setMessage("Are you sure you want to log out of the Bookfolio app?")
                positiveButton {
                    viewModel.signOut()
                    activity?.startActivityWithFlags(AuthActivity::class.java)
                }
                negativeButton("Cancel") {
                    it.dismiss()
                }
            }
        }
    }

}