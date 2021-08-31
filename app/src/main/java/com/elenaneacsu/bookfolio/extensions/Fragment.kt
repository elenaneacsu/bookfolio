package com.elenaneacsu.bookfolio.extensions

import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.elenaneacsu.bookfolio.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.*
import java.util.*
import kotlin.collections.ArrayList

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

fun Fragment.showMaterialDatePicker(
    isConstrained: Boolean = false,
    selectionDate: Long? = null,
    onCancelListener: DialogInterface.OnCancelListener,
    onPositiveButtonClickListener: MaterialPickerOnPositiveButtonClickListener<Long>,
) {
    val materialDatePicker =
        if (isConstrained) {
            val calendarStart = Calendar.getInstance()
            calendarStart.set(1900, 0, 0) // 1/1/1900
            val dateValidatorMin: CalendarConstraints.DateValidator =
                DateValidatorPointForward.from(calendarStart.timeInMillis)
            val dateValidatorMax: CalendarConstraints.DateValidator =
                DateValidatorPointBackward.before(System.currentTimeMillis())
            val listValidators = ArrayList<CalendarConstraints.DateValidator>()
            listValidators.add(dateValidatorMin)
            listValidators.add(dateValidatorMax)
            val validators = CompositeDateValidator.allOf(listValidators)
            MaterialDatePicker.Builder
                .datePicker()
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(validators)
                        .build()
                )
                .build()
        } else if (selectionDate != null) {
            MaterialDatePicker.Builder
                .datePicker()
                .setSelection(selectionDate)
                .build()
        } else {
            val today = Calendar.getInstance()
            MaterialDatePicker.Builder
                .datePicker()
                .setSelection(today.timeInMillis)
                .build()
        }
    materialDatePicker.addOnCancelListener(onCancelListener)
    materialDatePicker.addOnNegativeButtonClickListener {
        materialDatePicker.dismiss()
    }
    materialDatePicker.addOnPositiveButtonClickListener(onPositiveButtonClickListener)
    materialDatePicker.show(childFragmentManager, materialDatePicker.toString())
}

