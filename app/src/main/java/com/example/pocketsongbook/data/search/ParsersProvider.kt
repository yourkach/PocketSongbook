package com.example.pocketsongbook.data.search

import com.example.pocketsongbook.data.search.website_parsers.SongsWebsiteParser

interface ParsersProvider {
    val websiteParsers: List<SongsWebsiteParser>
}