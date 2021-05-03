package com.example.pocketsongbook.common

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.navigation.BackPressedListener
import com.example.pocketsongbook.common.navigation.BottomNavigationHelper
import com.example.pocketsongbook.common.navigation.NavigationTab
import com.example.pocketsongbook.common.navigation.TabCiceronesHolder
import com.example.pocketsongbook.common.navigation.impl.TabsFactoryImpl
import com.example.pocketsongbook.di.annotations.ActivityScope
import com.example.pocketsongbook.di.annotations.ApplicationScope
import com.example.pocketsongbook.di.modules.NavigationModule
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation_bar_layout.*
import moxy.MvpAppCompatActivity
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class RootActivity : MvpAppCompatActivity() {

    private val holder: TabCiceronesHolder by inject()

    private val navigationHelper by lazy {
        BottomNavigationHelper(
            containerViewId = R.id.rootContainer,
            fragmentManager = supportFragmentManager,
            tabsFactory = TabsFactoryImpl(),
            onTabChanged = ::onTabSwitched
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KTP.openScope(ApplicationScope::class.java)
            .openSubScope(ActivityScope::class.java)
            .openSubScope(this)
            .supportScopeAnnotation(ActivityScope::class.java)
            .installModules(NavigationModule())
            .closeOnDestroy(this)
            .inject(this)
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

    private fun onTabSwitched(tab: NavigationTab) {
        clearSelectedTabButton()
        val (imageView: ImageView, textView: TextView) = when (tab) {
            NavigationTab.Favorites -> navIvFavoritesIcon to navTvFavoritesLabel
            NavigationTab.Search -> navIvSearchIcon to navTvSearchLabel
            NavigationTab.Tuner -> navIvTunerIcon to navTvTunerLabel
        }
        imageView.imageTintList = ColorStateList.valueOf(selectedNavItemColor)
        textView.setTextColor(selectedNavItemColor)
    }

    private val selectedNavItemColor by lazy {
        ResourcesCompat.getColor(resources, R.color.colorNavItemSelected, null)
    }
    private val unselectedNavItemColor by lazy {
        ResourcesCompat.getColor(resources, R.color.colorNavItemUnselected, null)
    }

    private fun clearSelectedTabButton() {
        listOf<ImageView>(navIvFavoritesIcon, navIvSearchIcon, navIvTunerIcon).forEach {
            it.imageTintList = ColorStateList.valueOf(unselectedNavItemColor)
        }
        listOf<TextView>(navTvFavoritesLabel, navTvSearchLabel, navTvTunerLabel).forEach {
            it.setTextColor(unselectedNavItemColor)
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
    }
}
