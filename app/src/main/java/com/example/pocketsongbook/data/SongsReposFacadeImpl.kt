package com.example.pocketsongbook.data

import com.example.pocketsongbook.data.song_repos.SongsRepo
import com.example.pocketsongbook.domain.SongsReposFacade
import com.example.pocketsongbook.domain.model.Song
import com.example.pocketsongbook.domain.model.SongSearchItem

class SongsReposFacadeImpl(private val songsRepos: List<SongsRepo>) :
    SongsReposFacade {

    override fun getWebsiteNames(): List<String> = songsRepos.map { it.websiteName }

    private val defaultRepoIndex = 0

    private var currentRepoIndex = defaultRepoIndex


    /**
     * returns true if switch was successful
     */
    override fun switchToWebsite(position: Int): Boolean {
        return when {
            position !in songsRepos.indices -> {
                throw IndexOutOfBoundsException()
            }
            currentRepoIndex != position -> {
                currentRepoIndex = position
                true
            }
            else -> false
        }
    }

    override suspend fun getSearchResults(query: String): List<SongSearchItem>? =
        songsRepos[currentRepoIndex].getSearchResults(query)

    override suspend fun getSong(songSearchItem: SongSearchItem): Song? =
        songsRepos[currentRepoIndex].getSong(songSearchItem)
}