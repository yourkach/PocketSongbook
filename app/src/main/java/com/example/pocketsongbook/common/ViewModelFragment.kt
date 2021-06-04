package com.example.pocketsongbook.common

import androidx.annotation.LayoutRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pocketsongbook.feature.guitar_tuner.tuner_screen.TunerViewModel
import java.lang.IllegalStateException
import javax.inject.Provider

abstract class ViewModelFragment(@LayoutRes layoutId: Int) :
    BaseFragment(layoutId) {

    protected inline fun <reified TViewModel : ViewModel> viewModelCreator(crossinline createViewModel: () -> TViewModel): Lazy<TViewModel> {
        return viewModels(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                        return createViewModel() as? T ?: throw IllegalStateException()
                    }
                }
            }
        )
    }

}