package com.elenaneacsu.bookfolio.view.fragment

import androidx.annotation.LayoutRes
import com.elenaneacsu.bookfolio.view.BaseViewDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet(
    @LayoutRes private val contentLayoutId: Int
) : BottomSheetDialogFragment(), BaseViewDelegate {

    var apiRequestCompleted = false

}