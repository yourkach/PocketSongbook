package com.example.pocketsongbook.domain.search

enum class SongsWebsite(val websiteName: String) {
    AmDm("AmDm.ru"),
    MyChords("MyChords.net")
}

fun String.toSongsWebsiteOrNull(): SongsWebsite? {
    return SongsWebsite.values().firstOrNull { it.websiteName == this }
}