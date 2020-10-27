package com.example.pocketsongbook.utils

import android.os.SystemClock
import android.view.View


class SafeClickListener(val interval: Int = 1000, val safeClick: (View) -> Unit) : View.OnClickListener {

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

