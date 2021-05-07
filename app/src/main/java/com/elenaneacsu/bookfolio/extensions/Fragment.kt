package com.elenaneacsu.bookfolio.extensions

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

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
