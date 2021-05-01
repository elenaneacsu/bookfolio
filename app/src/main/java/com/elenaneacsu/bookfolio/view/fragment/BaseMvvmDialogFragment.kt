package com.elenaneacsu.bookfolio.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.elenaneacsu.bookfolio.vm.BaseViewModel

abstract class BaseMvvmDialogFragment<VM : BaseViewModel, VB : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int,
    private val viewModelClass: Class<VM>
) : BaseDialogFragment(contentLayoutId) {

    val viewModel: VM by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    private var _viewBinding: VB? = null

    // This property is only valid between onCreateView and onDestroyView.
    val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding =
            DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
        initViewModel()
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner

        initViews()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    open fun initViewModel() {

    }

    open fun initViews() {

    }

    open fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner, { message ->
            message?.let {
                errorAlert(it)
                viewModel.setError(null)
            }
        })
    }
}