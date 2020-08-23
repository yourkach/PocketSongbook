package com.example.pocketsongbook.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pocketsongbook.R
import com.example.pocketsongbook.ui.fragments.search.SearchFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

class RootActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var cicerone: Cicerone<Router>

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private val navigator by lazy {
        SupportAppNavigator(this, supportFragmentManager, R.id.rootContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        cicerone.router.newRootScreen(
            object : SupportAppScreen() {
                override fun getFragment(): Fragment = SearchFragment()
            }
        )

    }

    override fun onPause() {
        super.onPause()
        cicerone.navigatorHolder.removeNavigator()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        cicerone.navigatorHolder.setNavigator(navigator)
    }
}
