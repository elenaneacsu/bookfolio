package com.elenaneacsu.bookfolio.utils

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View

class OnOneOffClickListener(
    private val delayInMilliSeconds: Long,
    val action: (View) -> Unit,
) : View.OnClickListener {

    private var lastClickTime: Long = 0L

    private var isViewClicked = false


    override fun onClick(v: View?) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - lastClickTime

        lastClickTime = currentClickTime

        if (elapsedTime <= delayInMilliSeconds)
            return
        if (!isViewClicked) {
            isViewClicked = true
            startTimer()
        } else {
            return
        }
        action(v!!)
    }

    /**
     * This method delays simultaneous touch events of multiple views.
     */
    private fun startTimer() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            isViewClicked = false
        }, delayInMilliSeconds)
    }
}

fun View.setOnOneOffClickListener(delayInMilliSeconds: Long = 500, action: (View) -> Unit) {
    val onOneOffClickListener = OnOneOffClickListener(delayInMilliSeconds, action)
    setOnClickListener(onOneOffClickListener)
}