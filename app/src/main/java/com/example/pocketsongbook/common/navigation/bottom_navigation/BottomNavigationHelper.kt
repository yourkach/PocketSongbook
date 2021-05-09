package com.example.pocketsongbook.common.navigation.bottom_navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.pocketsongbook.common.BaseTabContainerFragment

class BottomNavigationHelper(
    @IdRes private val containerViewId: Int,
    private val fragmentManager: FragmentManager,
    private val tabsFactory: TabsFactory,
    private val onTabSwitch: OnTabSwitchedListener
) {

    private val tabContainersMap = mutableMapOf<NavigationTab, BaseTabContainerFragment>().apply {
        NavigationTab.values()
            .asSequence()
            .mapNotNull { tab ->
                (fragmentManager.findFragmentByTag(tab.name) as? BaseTabContainerFragment)?.let { tab to it }
            }
            .forEach { (tab, restoredFragment) ->
                put(tab, restoredFragment)
            }
    }

    private val tabsBackStack = mutableListOf<NavigationTab>()

    private var currentTab: NavigationTab? = null
        set(newTab) {
            if (newTab == null || field == newTab) return
            field.also { oldTab ->
                field = newTab
                onTabSwitch.onTabSwitched(oldTab, newTab)
            }
        }

    fun switchToTab(tab: NavigationTab, addToBackStack: Boolean = true) {
        if (currentTab != tab || tabContainersMap[tab]?.isVisible != true) {
            val transaction = fragmentManager.beginTransaction()
            if (addToBackStack) hideCurrentTab(transaction) else removeCurrentTab(transaction)
            val tabContainer = getAndRemoveFromStack(tab) ?: createTabContainer(tab)
            if (!tabContainer.isAdded) transaction.add(containerViewId, tabContainer, tab.name)
            if (tabContainer.isHidden) transaction.show(tabContainer)
            transaction.commitNow()
            currentTab = tab
        } else if (currentTab == tab && tabContainersMap[tab]?.isVisible == true) {
            tabContainersMap[tab]?.resetContainer()
        }
    }

    fun saveState(): Bundle {
        return Bundle().apply {
            putParcelableArray(BACKSTACK_KEY, tabsBackStack.toTypedArray())
            currentTab?.let { putParcelable(CURRENT_TAB_KEY, it) }
        }
    }

    fun restoreState(savedState: Bundle) {
        savedState.apply {
            getParcelableArray(BACKSTACK_KEY)?.mapNotNull { it as? NavigationTab }?.let {
                tabsBackStack.clear()
                tabsBackStack.addAll(it)
            }
            getParcelable<NavigationTab>(CURRENT_TAB_KEY)?.let {
                switchToTab(it)
            }
        }
    }

    private fun removeCurrentTab(transaction: FragmentTransaction) {
        currentTab?.let { tabContainersMap[it] }?.takeIf { it.isVisible }
            ?.let { currentContainer ->
                transaction.remove(currentContainer)
                tabContainersMap.remove(currentTab)
                currentTab?.let(tabsBackStack::remove)
            }
    }

    fun switchTabBack(): Boolean {
        return tabsBackStack.lastOrNull()
            ?.let { tab -> getAndRemoveFromStack(tab)?.let { tab to it } }
            ?.also { (tab, tabContainer) ->
                val transaction = fragmentManager.beginTransaction()
                removeCurrentTab(transaction)
                if (!tabContainer.isAdded) transaction.add(containerViewId, tabContainer, tab.name)
                if (tabContainer.isHidden) transaction.show(tabContainer)
                transaction.commitNow()
                currentTab = tab
            } != null
    }

    private fun hideCurrentTab(transaction: FragmentTransaction) {
        currentTab?.let { tabContainersMap[it] }?.takeIf { it.isVisible }
            ?.let { currentContainer ->
                transaction.hide(currentContainer)
                currentTab?.let(tabsBackStack::add)
            }
    }

    private fun getAndRemoveFromStack(tab: NavigationTab): BaseTabContainerFragment? {
        return tabContainersMap[tab]?.also { tabsBackStack.remove(tab) }
    }

    private fun createTabContainer(tab: NavigationTab): BaseTabContainerFragment {
        return tabsFactory.createTab(tab).also { container -> tabContainersMap[tab] = container }
    }

    companion object {
        private const val BACKSTACK_KEY = "BACKSTACK_KEY"
        private const val CURRENT_TAB_KEY = "CURRENT_TAB_KEY"
    }

}

