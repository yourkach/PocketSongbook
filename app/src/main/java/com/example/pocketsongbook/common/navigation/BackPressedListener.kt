package com.example.pocketsongbook.common.navigation

interface BackPressedListener {

    /**
     * @return true if event was consumed
     */
    fun onBackPressed(): Boolean

}

