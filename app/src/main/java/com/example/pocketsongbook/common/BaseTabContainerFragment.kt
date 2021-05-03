package com.example.pocketsongbook.common

import android.content.Context
import android.os.Bundle
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.navigation.BackPressedListener
import com.example.pocketsongbook.common.navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.RouterProvider
import com.example.pocketsongbook.di.annotations.ActivityScope
import com.example.pocketsongbook.di.annotations.ApplicationScope
import com.example.pocketsongbook.di.annotations.ContainerScope
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import moxy.MvpAppCompatFragment
import toothpick.ktp.KTP
import toothpick.smoothie.lifecycle.closeOnDestroy

abstract class BaseTabContainerFragment : MvpAppCompatFragment(R.layout.fragment_tab_container),
    RouterProvider, BackPressedListener {

    abstract val tab: NavigationTab

    abstract val rootScreen: Screen

    override val router: Router
        get() = cicerone.router

    private val cicerone: Cicerone<Router> by lazy {
        (activity as RootActivity).getTabCicerone(tab)
    }

    override fun onAttach(context: Context) {
        KTP.openScope(ApplicationScope::class.java)
            .openSubScope(ActivityScope::class.java)
            .openSubScope(activity)
            .openSubScope(this)
            .supportScopeAnnotation(ContainerScope::class.java)
            .closeOnDestroy(this)
            .inject(this)
        super.onAttach(context)
    }


    fun resetContainer() {
        if (childFragmentManager.backStackEntryCount > 1) {
            router.newRootScreen(rootScreen)
        }
    }

    private val navigator: Navigator by lazy {
        AppNavigator(
            requireActivity(),
            R.id.flFragmentContainer,
            fragmentManager = childFragmentManager
        )
    }

    override fun onBackPressed(): Boolean {
        return childOnBackPressed() || tabOnBackPressed()
    }

    private fun tabOnBackPressed(): Boolean {
        return if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            true
        } else false
    }

    private fun childOnBackPressed(): Boolean {
        return childFragmentManager.fragments.last()
            .let { it as? BackPressedListener }?.onBackPressed() ?: false
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

