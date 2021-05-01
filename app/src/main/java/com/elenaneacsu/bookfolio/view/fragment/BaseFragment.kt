package com.elenaneacsu.bookfolio.view.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.elenaneacsu.bookfolio.view.BaseViewDelegate

/**
 * Created by Grigore Cristian-Andrei on 07.08.2020.
 */
abstract class BaseFragment(
    @LayoutRes private val contentLayoutId: Int
) : Fragment(), BaseViewDelegate {

    var apiRequestCompleted = false

}