package com.example.pocketsongbook.ui.navigation

import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import moxy.MvpAppCompatFragment

abstract class ArgsFragment<TArgs : FragmentArgs<*>>(
    @LayoutRes contentLayoutId: Int
) : MvpAppCompatFragment(contentLayoutId) {

    companion object {
        const val ARGUMENTS_KEY = "arguments"
    }

    private var _args: TArgs? = null

    var args: TArgs
        get() {
            if (_args == null) {
                _args = arguments?.getParcelable(ARGUMENTS_KEY)
            }
            return _args!!
        }
        set(args) {
            arguments = bundleOf(ARGUMENTS_KEY to args)
            _args = args
        }

}

