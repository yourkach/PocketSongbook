package com.example.pocketsongbook.common

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.navigation.BackPressedListener
import com.example.pocketsongbook.common.navigation.TabCiceronesHolder
import com.example.pocketsongbook.common.navigation.bottom_navigation.BottomNavigationHelper
import com.example.pocketsongbook.common.navigation.bottom_navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.bottom_navigation.OnTabSwitchedListener
import com.example.pocketsongbook.common.navigation.impl.TabsFactoryImpl
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation_bar_layout.*
import moxy.MvpAppCompatActivity
import javax.inject.Inject

class RootActivity : MvpAppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var holder: TabCiceronesHolder

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private val navigationHelper by lazy {
        BottomNavigationHelper(
            containerViewId = R.id.rootContainer,
            fragmentManager = supportFragmentManager,
            tabsFactory = TabsFactoryImpl(),
            onTabChanged = ::onTabSwitched
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            navigationHelper.switchToTab(NavigationTab.Search)
        } else {
            savedInstanceState.getBundle(NAVIGATION_STATE_KEY)?.let {
                navigationHelper.restoreState(it)
            }
        }
        setNavigationButtonsListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(NAVIGATION_STATE_KEY, navigationHelper.saveState())
    }

    private fun setNavigationButtonsListeners() {
        navFavoritesFrame.setOnClickListener {
            navigationHelper.switchToTab(NavigationTab.Favorites)
        }
        navSearchFrame.setOnClickListener {
            navigationHelper.switchToTab(NavigationTab.Search)
        }
        navTunerFrame.setOnClickListener {
            navigationHelper.switchToTab(NavigationTab.Tuner)
        }
    }

    private val bottomNavigationTabViews by lazy {
        mapOf(
            NavigationTab.Favorites to NavigationTabViews(navIvFavoritesIcon, navTvFavoritesLabel),
            NavigationTab.Search to NavigationTabViews(navIvSearchIcon, navTvSearchLabel),
            NavigationTab.Tuner to NavigationTabViews(navIvTunerIcon, navTvTunerLabel)
        )
    }


    private val selectedNavItemColorStateList by lazy {
        ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.colorNavItemSelected, null))
    }
    private val unselectedNavItemColorStateList by lazy {
        ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.colorNavItemUnselected, null))
    }
    private fun onTabSwitched(newTab: NavigationTab) {
        bottomNavigationTabViews.forEach { (tab, views) ->
            val (newColor, newScale) = when (tab) {
                newTab -> selectedNavItemColorStateList to TAB_ICON_SCALE_SELECTED
                else -> unselectedNavItemColorStateList to 1.0f
            }

            views.tabIconView.imageTintList = newColor
            views.tabTitleTextView.setTextColor(newColor)

            views.tabIconView.animate().apply {
                scaleX(newScale)
                scaleY(newScale)
                duration = TAB_ICON_ANIMATION_DURATION
                start()
            }
        }
        supportFragmentManager.fragments.forEach { childFragment ->
            (childFragment as? OnTabSwitchedListener)?.onTabSwitched(newTab = newTab)
        }
    }

    override fun onBackPressed() {
        val childrenConsumedEvent = supportFragmentManager.run {
            (fragments.firstOrNull { it.isVisible } as? BackPressedListener)?.onBackPressed()
                ?: false
        }
        if (!childrenConsumedEvent && !navigationHelper.switchTabBack()) {
            super.onBackPressed()
        }
    }

    fun getTabCicerone(tab: NavigationTab): Cicerone<Router> {
        return holder.getCicerone(tab)
    }

    fun setNavigationBarVisible(isVisible: Boolean) {
        bottomNavigationBar.isVisible = isVisible
    }

    fun showLoading() {
        loadingStub.isVisible = true
    }

    fun hideLoading() {
        loadingStub.isVisible = false
    }

    companion object {
        private const val NAVIGATION_STATE_KEY = "NAVIGATION_STATE_KEY"
        private const val TAB_ICON_ANIMATION_DURATION = 100L
        private const val TAB_ICON_SCALE_SELECTED = 1.2f
    }
}
