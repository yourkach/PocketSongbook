package com.example.pocketsongbook.common.navigation

interface BackPressedListener {

    /**
     * @return true if event consumed
     */
    fun onBackPressed(): Boolean

}