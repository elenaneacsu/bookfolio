package com.elenaneacsu.bookfolio.extensions

import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Created by Grigore Cristian-Andrei on 16.03.2021.
 */
fun TextView.makeLinks(@ColorRes colorId: Int, vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
                textPaint.color = ContextCompat.getColor(context, colorId)
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                /*  Selection.setSelection((view as TextView).text as Spannable, 0)
                  view.invalidate()*/
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan,
            startIndexOfLink,
            startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}


fun TextView.showUnderline(show: Boolean = true) {
    paintFlags =
        if (show) paintFlags or Paint.UNDERLINE_TEXT_FLAG
        else paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
}

fun TextView.disableClick() {
    isEnabled = false
    isClickable = false
}

fun TextView.enableClick() {
    isEnabled = true
    isClickable = true
}

fun EditText.disableClick() {
    isEnabled = false
    isClickable = false
}

fun EditText.enableClick() {
    isEnabled = true
    isClickable = true
}

fun EditText.enableOrDisableClick() {
    isEnabled = !isEnabled
    isClickable = !isClickable
}