package com.example.pocketsongbook.ui.navigation

import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen


interface FragmentArgs<TFragment : Fragment> : Parcelable


// navigation extensions -----------------------------------------------
// TODO: 23.08.20 вынести в отдельный файл

inline fun <reified TFragment : ArgsFragment<TArgs>, TArgs : FragmentArgs<TFragment>> TArgs.toFragment(): TFragment {
    return TFragment::class.java.newInstance()
        .apply { arguments = bundleOf(ArgsFragment.ARGUMENTS_KEY to this@toFragment) }
}

//inline fun <reified TFragment : ParcelableArgsFragment<TArgs>, TArgs : ParcelableFragmentArgs<TFragment>>
//        TArgs.toFragment(): TFragment {
//
//    val fragment = TFragment::class.java.newInstance()
//    fragment.arguments = bundleOf(ParcelableArgsFragment.ARGS to this)
//    return fragment
//}

fun Router.navigateToFragment(f: Fragment) {
    navigateTo(
        object : SupportAppScreen() {
            override fun getFragment(): Fragment = f
        }
    )
}