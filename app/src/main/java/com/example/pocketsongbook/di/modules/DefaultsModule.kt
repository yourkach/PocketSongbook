package com.example.pocketsongbook.di.modules

import com.example.pocketsongbook.domain.song.DefaultsProvider
import com.ybond.core_entities.models.FontChangeDefaults
import com.ybond.core_entities.models.KeyChangeDefaults
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DefaultsModule {

    @Provides
    @Singleton
    fun provideKeyDefaults(provider: DefaultsProvider): KeyChangeDefaults =
        provider.getKeyDefaults()

    @Provides
    @Singleton
    fun provideFontDefaults(provider: DefaultsProvider): FontChangeDefaults =
        provider.getFontDefaults()

}

