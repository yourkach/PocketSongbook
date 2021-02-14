package com.example.pocketsongbook.utils

import android.app.Activity
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes


class SafeClickListener(val interval: Int = 1000, val safeClick: (View) -> Unit) :
    View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < interval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        safeClick(v)
    }
}


fun View.setOnSafeClickListener(interval: Int = 1000, onSafeClick: (View) -> Unit) {

    val safeClickListener = SafeClickListener(interval = interval, safeClick = onSafeClick)
    setOnClickListener(safeClickListener)
}


fun View.hideKeyboard() {
    val inputManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputManager?.hideSoftInputFromWindow(this.windowToken, 0)
    this.clearFocus()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Any.logDebug(text: String) {
    Log.d(this::class.simpleName, text)
}