package com.elenaneacsu.bookfolio.view.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.elenaneacsu.bookfolio.vm.BaseViewModel

abstract class BaseMvvmActivity<VM : BaseViewModel, VB : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int,
    private val viewModelClass: Class<VM>
) : BaseActivity(contentLayoutId) {

    val viewModel: VM by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    lateinit var viewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, contentLayoutId)
        viewBinding.lifecycleOwner = this
        initObservers()
    }

    open fun initViewModel() {

    }

    open fun initViews() {

    }
    open fun initObservers() {
        viewModel.error.observe(this, { message ->
            message?.let {
                errorAlert(it)
                viewModel.setError(null)
            }
        })
    }

}
