package com.example.pocketsongbook.common

import android.os.Bundle
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.navigation.BackPressedListener
import com.example.pocketsongbook.common.navigation.RouterProvider
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import moxy.MvpAppCompatFragment

abstract class BaseTabContainerFragment : MvpAppCompatFragment(R.layout.fragment_tab_container),
    RouterProvider, BackPressedListener {

    abstract val tabName: String

    abstract val rootScreen: Screen

    override val router: Router
        get() = cicerone.router

    private val cicerone: Cicerone<Router> by lazy {
        (activity as RootActivity).getTabCicerone(tabName)
    }

    fun resetContainer() {
        router.newRootScreen(rootScreen)
    }

    private val navigator: Navigator by lazy {
        AppNavigator(
            requireActivity(),
            R.id.flFragmentContainer,
            fragmentManager = childFragmentManager
        )
    }

    override fun onBackPressed(): Boolean {
        return if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            true
        } else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) router.newRootScreen(rootScreen)
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

}

