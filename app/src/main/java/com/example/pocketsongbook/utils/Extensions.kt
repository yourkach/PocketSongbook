package com.example.pocketsongbook.utils

import android.app.Activity
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.annotation.LayoutRes
import timber.log.Timber


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

var SearchView.isViewFocused: Boolean
    get() = this.isFocused || this.focusedChild != null
    set(value) {
        if (value == this.isViewFocused) return
        if (value) requestFocus() else clearFocus()
    }

var SearchView.queryText: String
    get() = query.toString()
    set(value) {
        if (value == queryText) return
        setQuery(value, false)
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

fun Any.logTagged(text: String) {
    Timber.tag(this::class.simpleName).d(text)
}