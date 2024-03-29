package com.elenaneacsu.bookfolio.view.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.elenaneacsu.bookfolio.view.BaseViewDelegate

abstract class BaseDialogFragment(
    @LayoutRes private val contentLayoutId: Int
) : DialogFragment(), BaseViewDelegate {

    var apiRequestCompleted = false

}