package com.example.pocketsongbook.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment

abstract class BaseFragment(@LayoutRes layoutId: Int) : MvpAppCompatFragment(layoutId), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun showLoading() {
        (activity as? RootActivity)?.showLoading()
    }

    override fun hideLoading() {
        (activity as? RootActivity)?.hideLoading()
    }

    override fun showMessage(messageId: Int) {
        TODO("Not yet implemented")
    }

}