package com.elenaneacsu.bookfolio.view.fragment

import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.elenaneacsu.bookfolio.view.BaseViewDelegate

/**
 * Created by Grigore Cristian-Andrei on 07.08.2020.
 */
abstract class BaseBottomSheet(
    @LayoutRes private val contentLayoutId: Int
) : BottomSheetDialogFragment(), BaseViewDelegate {

    var apiRequestCompleted = false

}