package com.elenaneacsu.bookfolio.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


/**
 * show toast method
 *
 * @param message string to display as toast message
 * @param length length of the toast (LENGTH_SHORT - default)
 */
fun Activity.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

/**
 * show toast method
 *
 * @param resource string id
 * @param length length of the toast (LENGTH_SHORT - default)
 */
fun Activity.toast(@StringRes resource: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resource, length).show()
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

/**
 * add fragment method
 *
 * @param fragment fragment to be added
 * @param frameId layout id
 */
fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        add(frameId, fragment, fragment.javaClass.canonicalName)
        addToBackStack(fragment.javaClass.canonicalName)
        setReorderingAllowed(true)
    }
}

/**
 * replace fragment method
 *
 * @param fragment fragment to be replaced
 * @param frameId layout id
 */
fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, args: Bundle? = null) {
    supportFragmentManager.inTransaction {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        replace(frameId, fragment.javaClass, args, fragment.javaClass.canonicalName)
        addToBackStack(fragment.javaClass.canonicalName)
        setReorderingAllowed(true)
    }
}

fun AppCompatActivity.findCurrentFragment(): Fragment? {
    supportFragmentManager.let {
        return if (it.fragments.size >= 1)
            it.fragments[it.fragments.size - 1]
        else null
    }
}

/**
 * replace fragment method
 *
 * @param fragment fragment to be replaced
 * @param frameId layout id
 */
fun FragmentManager.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    addToBackStack: Boolean = true,
) {
    beginTransaction().apply {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        replace(frameId, fragment, fragment.javaClass.canonicalName)
        if (addToBackStack) addToBackStack(fragment.javaClass.canonicalName)
        setReorderingAllowed(true)
    }.commit()
}

/**
 * pop back stack
 */
fun AppCompatActivity.popBackStack() {
    if (supportFragmentManager.backStackEntryCount == 1) {
        finish()
    } else {
//        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStackImmediate()
    }
}


fun AppCompatActivity.popBackStackNow() {
    supportFragmentManager.popBackStackImmediate()
}

/**
 * get last fragment
 *
 * @return returns the last fragment
 */
fun AppCompatActivity.getLastFragment(): Fragment {
    return if (supportFragmentManager.fragments.size == 0) {
        Fragment()
    } else {
        supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1]
    }
}


/**
 * Hide keyboard programmatically
 *
 * @param view remove focus from
 */
fun AppCompatActivity.hideKeyboard(view: View) {
    val imm =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * For future calls on hideKeyboard(Activity activity) method
 * should be replaced with this method
 *
 * view should be an instance of [EditText]
 * eg:
 * @return state of keyboard (hide or not)
 */
fun View.hideKeyboard(): Boolean {
    if (this is EditText) {
        this.onEditorAction(EditorInfo.IME_ACTION_DONE)
        return true
    }
    return false
}

/**
 * Start activity with FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
 *
 * @param `class` - class of the activity to start
 */
fun <T> AppCompatActivity.startActivityWithFlags(`class`: Class<T>) {
    val intent = Intent(this, `class`)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    finishAffinity()
    startActivity(intent)
}

/**
 * Start activity with FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
 *
 * @param `class` - class of the activity to start
 */
fun <T> FragmentActivity.startActivityWithFlags(`class`: Class<T>) {
    val intent = Intent(this, `class`)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    finishAffinity()
    startActivity(intent)
}

/**
 * Start activity with FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
 *
 * @param `class` - class of the activity to start
 */
fun <T> Application.startActivityWithFlags(`class`: Class<T>) {
    val intent = Intent(this, `class`)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

/**
 * Start activity
 *
 * @param `class` - class of the activity to start
 */
fun <T> AppCompatActivity.startActivity(`class`: Class<T>) {
    val intent = Intent(this, `class`)
    startActivity(intent)
}

/**
 * Change the colour of the status bar
 */
fun AppCompatActivity.updateStatusBarColor(color: Int?, isStatusBarLight: Boolean) {
    val window: Window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    color?.let { window.statusBarColor = this.resources.getColor(it) }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = getWindow().decorView
        view.systemUiVisibility = if (isStatusBarLight)
            view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

fun AppCompatActivity.logDebug(msg: String?) {
    Log.d(javaClass.simpleName, msg ?: "@msg is null")
}

fun AppCompatActivity.logError(msg: String?, tr: Throwable?) {
    Log.e(javaClass.simpleName, msg, tr)
}
