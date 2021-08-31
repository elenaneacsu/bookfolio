package com.elenaneacsu.bookfolio.ui.account

import android.text.InputType
import android.widget.EditText
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentAccountBinding
import com.elenaneacsu.bookfolio.extensions.*
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

        viewModel.getUserDetails()

        viewBinding.apply {
            logOut.setOnClickListener {
                showLogOutDialog()
            }

            editName.setOnOneOffClickListener {
                showEditNameDialog()
            }

            deleteAccount.setOnOneOffClickListener {
                showDeleteAccountDialog()
            }
        }
    }

    override fun initObservers() {
        viewModel.userDataResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    viewBinding.user = it.data
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
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

    private fun showDeleteAccountDialog() {
        context?.let { ctx ->
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setTitle("Delete account")
                setMessage("Are you sure you want to delete your Bookfolio account?\nThis is a permanent action that cannot be undone.")
                positiveButton("Yes, delete it") {
                    viewModel.signOut()
                    activity?.startActivityWithFlags(AuthActivity::class.java)
                }
                negativeButton("Cancel") {
                    it.dismiss()
                }
            }
        }
    }

    private fun showEditNameDialog() {
        context?.let { ctx ->
            val editText = EditText(ctx).apply {
                inputType = InputType.TYPE_CLASS_TEXT
                setText(viewModel.getName())
            }
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setTitle("Type your name...")
                setView(editText)
                positiveButton("Save") {
                    viewModel.updateUserName(
                        editText.text?.toString()
                    )
                }
                negativeButton("Cancel") {
                    it.dismiss()
                }
            }
        }
    }

}