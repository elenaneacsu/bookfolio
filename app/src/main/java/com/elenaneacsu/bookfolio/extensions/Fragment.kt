package com.elenaneacsu.bookfolio.extensions

import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.ui.search.book_details.ShelfOptionsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * show toast method
 *
 * @param message string to display as toast message
 * @param length length of the toast (LENGTH_SHORT - default)
 */
fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, length).show()
}

fun Fragment.logDebug(msg: String?) {
    Log.d(javaClass.simpleName, msg ?: "@msg is null")
}

fun Fragment.logError(msg: String?, tr: Throwable?) {
    Log.e(javaClass.simpleName, msg, tr)
}

/**
 * create BottomSheetDialog instance with options in a recyclerView
 *
 * @param view the host view
 * @param title title of the dialog
 */
fun Fragment.createBottomSheetDialog(view: View,
                                     title: String? = null): BottomSheetDialog? =
    activity?.let {
        BottomSheetDialog(it).apply {
            setContentView(view)

            //for now, the recyclerview is set from concrete classes

            val titleTextview = view.findViewById<TextView>(R.id.title)
            titleTextview.text = title

            setContentView(view)

            setOnShowListener { dialog ->
                val d = dialog as BottomSheetDialog
                val bottomSheet =
                    d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout

                bottomSheet?.let {
                    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            setCancelable(true)
        }
    }

