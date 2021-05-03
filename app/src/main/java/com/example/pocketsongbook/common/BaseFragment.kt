package com.example.pocketsongbook.common

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.transition.TransitionInflater
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.navigation.BackPressedListener
import com.example.pocketsongbook.di.annotations.ActivityScope
import com.example.pocketsongbook.di.annotations.ApplicationScope
import com.example.pocketsongbook.di.annotations.FragmentScope
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import toothpick.config.Module
import toothpick.ktp.KTP
import toothpick.smoothie.lifecycle.closeOnDestroy

abstract class BaseFragment(@LayoutRes layoutId: Int) : MvpAppCompatFragment(layoutId),
    BaseView, BackPressedListener {

    open val enterTransitionRes: Int? = R.transition.fade

    open val returnTransitionRes: Int? = null

    open val hideBottomNavigationBar: Boolean = false

    protected val router: Router
        get() = (parentFragment as BaseTabContainerFragment).router

    open fun getToothpickModules(): List<Module> = emptyList()

    override fun onAttach(context: Context) {
        KTP.openScope(ApplicationScope::class.java)
            .openSubScope(ActivityScope::class.java)
            .openSubScope(activity)
            .openSubScope(parentFragment)
            .openSubScope(this)
            .supportScopeAnnotation(FragmentScope::class.java)
            .installModules(*getToothpickModules().toTypedArray())
            .closeOnDestroy(this)
            .inject(this)
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
            backgroundColorId?.let {
                this.setBackgroundTint(context.resources.getColor(backgroundColorId, context.theme))
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