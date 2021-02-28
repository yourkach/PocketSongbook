package com.example.pocketsongbook.utils

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchLayoutManager @JvmOverloads constructor(
    context: Context,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int,
        defStyleRes: Int
    ) : this(context) {
        val properties = getProperties(context, attrs, defStyleAttr, defStyleRes)
        orientation = properties.orientation
        reverseLayout = properties.reverseLayout
        stackFromEnd = properties.stackFromEnd
    }

    var isScrollingEnabled: Boolean = true

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putParcelable(PARENT_SAVED_STATE_KEY, super.onSaveInstanceState())
            putBoolean(SCROLLING_ENABLED_KEY, isScrollingEnabled)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? Bundle)?.let { bundle ->
            bundle.getParcelable<SavedState>(PARENT_SAVED_STATE_KEY)?.let { parentState ->
                super.onRestoreInstanceState(parentState)
            }
            isScrollingEnabled = bundle.getBoolean(SCROLLING_ENABLED_KEY, true)
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return isScrollingEnabled && super.canScrollHorizontally()
    }

    override fun canScrollVertically(): Boolean {
        return isScrollingEnabled && super.canScrollVertically()
    }

    companion object {
        private const val PARENT_SAVED_STATE_KEY = "PARENT_SAVED_STATE_KEY"
        private const val SCROLLING_ENABLED_KEY = "SCROLLING_ENABLED_KEY"
    }

}