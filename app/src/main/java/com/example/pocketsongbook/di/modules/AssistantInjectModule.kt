package com.example.pocketsongbook.di.modules

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_AssistantInjectModule::class])
object AssistantInjectModule