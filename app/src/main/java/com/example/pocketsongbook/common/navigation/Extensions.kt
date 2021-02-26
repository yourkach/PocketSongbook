package com.example.pocketsongbook.common.navigation

import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

fun Fragment.toScreen(): Screen {
    return FragmentScreen(
        screenKey = this::class.java.simpleName + hashCode(),
        createFragment = { this }
    )
}