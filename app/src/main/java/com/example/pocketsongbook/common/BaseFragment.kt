package com.example.pocketsongbook.common

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjector
import com.example.pocketsongbook.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes layoutId: Int) : MvpAppCompatFragment(layoutId),
    BaseView, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    open val enterTransitionRes: Int? = R.transition.fade

    open val returnTransitionRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        initTransitions()
    }

    private fun initTransitions() {
        with(TransitionInflater.from(requireContext())) {
            enterTransitionRes?.let { enterTransition = inflateTransition(it) }
            returnTransitionRes?.let { returnTransition = inflateTransition(it) }
        }
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
        // TODO: 31.10.20 сделать цвет для сообщений об ошибке
        showInfoSnackBar(message)
    }

    protected fun showInfoSnackBar(
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

    protected fun showActionSnackBar(
        text: String,
        buttonText: String,
        onButtonClick: () -> Unit
    ): Snackbar {
        return Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).apply {
            setAction(buttonText) { onButtonClick() }
            show()
        }
    }

}