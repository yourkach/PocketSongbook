package com.example.pocketsongbook.utils

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.annotation.LayoutRes
import androidx.core.animation.addListener
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

inline fun View.startViewPropertyAnimator(
    crossinline configureAnimator: ViewPropertyAnimator.() -> Unit,
    crossinline onStart: (Animator?) -> Unit = {},
    crossinline onEnd: (Animator?) -> Unit = {},
    crossinline onCancel: (Animator?) -> Unit = {},
    crossinline onRepeat: (Animator?) -> Unit = {},
) {
    animate().apply {
        configureAnimator()
        setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = onStart(animation)
            override fun onAnimationEnd(animation: Animator?) = onEnd(animation)
            override fun onAnimationCancel(animation: Animator?) = onCancel(animation)
            override fun onAnimationRepeat(animation: Animator?) = onRepeat(animation)
        })
    }.start()
}

inline fun startFloatAnimator(
    fromValue: Float,
    toValue: Float,
    durationMillis: Long,
    crossinline onUpdate: (Float) -> Unit,
    timeInterpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    crossinline onStart: (Animator) -> Unit = {},
    crossinline onRepeat: (Animator) -> Unit = {},
    crossinline onEnd: (Animator) -> Unit = {},
    crossinline onCancel: (Animator) -> Unit = {},
): Animator = ValueAnimator.ofFloat(fromValue, toValue)
    .apply {
        interpolator = timeInterpolator
        duration = durationMillis
        addUpdateListener { onUpdate(this.animatedValue as Float) }
        addListener(
            onEnd = onEnd,
            onStart = onStart,
            onRepeat = onRepeat,
            onCancel = onCancel
        )
        start()
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
