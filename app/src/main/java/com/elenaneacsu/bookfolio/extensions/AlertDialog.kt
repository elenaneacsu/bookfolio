package com.elenaneacsu.bookfolio.extensions

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StyleRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Created by Elena Neacsu on 19/06/21
 */
fun Context.alert(
    @StyleRes style: Int = 0,
    cancelable: Boolean = false,
    dialogBuilder: MaterialAlertDialogBuilder.() -> Unit
) {
    MaterialAlertDialogBuilder(this, style)
        .apply {
            setCancelable(cancelable)
            dialogBuilder()
            create()
            show()
        }
}

fun MaterialAlertDialogBuilder.negativeButton(
    text: String = "No",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setNegativeButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun MaterialAlertDialogBuilder.positiveButton(
    text: String = "Yes",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setPositiveButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun MaterialAlertDialogBuilder.neutralButton(
    text: String = "OK",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setNeutralButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}