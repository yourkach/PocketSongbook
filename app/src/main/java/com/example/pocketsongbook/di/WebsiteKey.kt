package com.example.pocketsongbook.di

import com.example.pocketsongbook.domain.search.SongsWebsite
import dagger.MapKey

@MapKey
annotation class WebsiteKey(val value: SongsWebsite)
