package com.example.pocketsongbook.common

import android.content.Context
import android.os.Bundle
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.navigation.BackPressedListener
import com.example.pocketsongbook.common.navigation.RouterProvider
import com.example.pocketsongbook.common.navigation.bottom_navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.bottom_navigation.OnTabSwitchedListener
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment
import javax.inject.Inject

abstract class BaseTabContainerFragment : MvpAppCompatFragment(R.layout.fragment_tab_container),
    RouterProvider, BackPressedListener, OnTabSwitchedListener, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    abstract val tab: NavigationTab

    abstract val rootScreen: Screen

    override val router: Router
        get() = cicerone.router

    private val cicerone: Cicerone<Router> by lazy {
        (activity as RootActivity).getTabCicerone(tab)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    fun resetContainer() {
        if (childFragmentManager.backStackEntryCount > 1) {
            router.newRootScreen(rootScreen)
        }
    }

    override fun onTabSwitched(oldTab: NavigationTab?, newTab: NavigationTab) {
        childFragmentManager.fragments.forEach { childFragment ->
            (childFragment as? OnTabSwitchedListener)?.onTabSwitched(oldTab, newTab)
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

