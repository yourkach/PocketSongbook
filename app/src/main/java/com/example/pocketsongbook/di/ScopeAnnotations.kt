package com.example.pocketsongbook.di

import javax.inject.Scope

@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Retention(value = AnnotationRetention.RUNTIME)
annotation class ContainerScope

@Retention(value = AnnotationRetention.RUNTIME)
annotation class FragmentScope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class UiComponentScope