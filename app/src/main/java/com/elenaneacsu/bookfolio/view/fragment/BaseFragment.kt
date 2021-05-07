package com.elenaneacsu.bookfolio.view.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.elenaneacsu.bookfolio.view.BaseViewDelegate

abstract class BaseFragment(
    @LayoutRes private val contentLayoutId: Int
) : Fragment(), BaseViewDelegate {

    var apiRequestCompleted = false

}