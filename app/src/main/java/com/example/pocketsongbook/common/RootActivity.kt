package com.example.pocketsongbook.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.feature.search.SearchFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class RootActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var cicerone: Cicerone<Router>

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private val navigator by lazy {
        AppNavigator(
            activity = this,
            containerId = R.id.rootContainer
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cicerone.router.newRootScreen(SearchFragment().toScreen())
    }

    fun showLoading() {
        loadingStub.isVisible = true
    }

    fun hideLoading() {
        loadingStub.isVisible = false
    }

    override fun onPause() {
        super.onPause()
        cicerone.getNavigatorHolder().removeNavigator()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }
}
