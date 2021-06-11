package com.ybond.data.repositories_impl

import com.ybond.core_entities.models.FoundSongModel
import com.ybond.core_entities.models.SongModel
import com.ybond.core_entities.models.SongsWebsite
import com.ybond.data_network.websites.WebsiteDataSourcesHolder
import com.ybond.domain.repositories.SongsRemoteRepository
import javax.inject.Inject

class SongsRemoteRepositoryImpl @Inject constructor(
    private val sourcesHolder: WebsiteDataSourcesHolder
) : SongsRemoteRepository {

    override suspend fun loadSearchResults(
        website: SongsWebsite,
        query: String
    ): List<FoundSongModel> {
        return sourcesHolder.getDataSource(website).loadSearchResults(query)
    }

    override suspend fun loadSong(songSearchItem: FoundSongModel): SongModel {
        return sourcesHolder.getDataSource(songSearchItem.website).loadSong(songSearchItem)
    }
}