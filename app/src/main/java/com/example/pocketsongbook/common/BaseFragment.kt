package com.example.pocketsongbook.common

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.transition.TransitionInflater
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.navigation.BackPressedListener
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes layoutId: Int) : MvpAppCompatFragment(layoutId),
    BaseView, HasAndroidInjector, BackPressedListener {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    open val enterTransitionRes: Int? = R.transition.fade

    open val returnTransitionRes: Int? = null

    open val hideBottomNavigationBar: Boolean = false

    protected val router: Router
        get() = (parentFragment as BaseTabContainerFragment).router

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTransitions()
    }

    override fun onBackPressed(): Boolean = false

    private fun initTransitions() {
        with(TransitionInflater.from(requireContext())) {
            enterTransitionRes?.let { enterTransition = inflateTransition(it) }
            returnTransitionRes?.let { returnTransition = inflateTransition(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        if (hideBottomNavigationBar) (activity as? RootActivity)?.setNavigationBarVisible(false)
    }

    override fun onPause() {
        super.onPause()
        if (hideBottomNavigationBar) (activity as? RootActivity)?.setNavigationBarVisible(true)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun showLoading() {
        (activity as? RootActivity)?.showLoading()
    }

    override fun hideLoading() {
        (activity as? RootActivity)?.hideLoading()
    }

    override fun showMessage(stringId: Int) {
        showInfoSnackBar(getString(stringId))
    }

    override fun showError(message: String) {
        showInfoSnackBar(message, R.color.colorErrorBackground)
    }

    private fun showInfoSnackBar(
        text: String,
        @ColorRes backgroundColorId: Int? = null
    ): Snackbar {
        return Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).apply {
            backgroundColorId?.let { id ->
                setBackgroundTint(ResourcesCompat.getColor(context.resources, id, context.theme))
            }
            show()
        }
    }

    protected fun isPermissionGranted(
        permissionName: String
    ): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        permissionName
    ) == PackageManager.PERMISSION_GRANTED

    protected fun showActionSnackBar(
        text: String,
        buttonText: String,
        anchorView: CoordinatorLayout? = null,
        onButtonClick: () -> Unit
    ): Snackbar {
        return Snackbar.make(anchorView ?: requireView(), text, Snackbar.LENGTH_LONG).apply {
            setAction(buttonText) { onButtonClick() }
            show()
        }
    }

}