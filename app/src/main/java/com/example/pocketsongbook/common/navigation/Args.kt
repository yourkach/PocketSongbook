package com.example.pocketsongbook.common.navigation

import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen


interface FragmentArgs<TFragment : Fragment> : Parcelable


// navigation extensions -----------------------------------------------
// TODO: 23.08.20 вынести в отдельный файл

inline fun <reified TFragment : ArgsFragment<TArgs>, TArgs : FragmentArgs<TFragment>> TArgs.toFragment(): TFragment {
    return TFragment::class.java.newInstance()
        .apply { arguments = bundleOf(ArgsFragment.ARGUMENTS_KEY to this@toFragment) }
}

fun Router.navigateToFragment(fragment: Fragment) {
    navigateTo(
        FragmentScreen(
            screenKey = fragment::class.java.simpleName,
            createFragment = { fragment }
        )
    )
}

fun Fragment.toScreen(): Screen {
    return FragmentScreen(
        screenKey = this::class.java.simpleName,
        createFragment = { this }
    )
}