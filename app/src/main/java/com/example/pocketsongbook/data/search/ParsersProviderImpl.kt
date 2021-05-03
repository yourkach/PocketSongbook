package com.example.pocketsongbook.data.search

import com.example.pocketsongbook.data.search.website_parsers.AmdmWebsiteParser
import com.example.pocketsongbook.data.search.website_parsers.MychordsWebsiteParser
import com.example.pocketsongbook.data.search.website_parsers.SongsWebsiteParser
import toothpick.InjectConstructor
import javax.inject.Singleton

@Singleton
@InjectConstructor
class ParsersProviderImpl : ParsersProvider {
    override val websiteParsers: List<SongsWebsiteParser> by lazy {
        listOf(AmdmWebsiteParser(), MychordsWebsiteParser())
    }
}