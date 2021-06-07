package com.example.pocketsongbook.common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalStateException

inline fun <reified TViewModel : ViewModel> Fragment.viewModelCreator(
    crossinline createViewModel: () -> TViewModel
): Lazy<TViewModel> {
    return viewModels(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return createViewModel() as? T ?: throw IllegalStateException()
                }
            }
        }
    )
}