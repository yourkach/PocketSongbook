package com.example.pocketsongbook.data.favorites

import com.example.pocketsongbook.data.database.entities.FavoriteSongEntity
import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.search.SongsWebsite
import javax.inject.Inject

class FavoriteModelMapper @Inject constructor() {

    fun toModel(entity: FavoriteSongEntity): FavoriteSongModel {
        return FavoriteSongModel(
            song = SongModel(
                artist = entity.artist,
                title = entity.title,
                lyrics = entity.lyrics,
                url = entity.url,
                website = SongsWebsite.valueOf(entity.website_name)
            ),
            timeAdded = entity.time_added
        )
    }

    fun toEntity(model: FavoriteSongModel): FavoriteSongEntity {
        return model.song.let { song ->
            FavoriteSongEntity(
                artist = song.artist,
                title = song.title,
                lyrics = song.lyrics,
                url = song.url,
                time_added = model.timeAdded,
                website_name = song.website.name
            )
        }
    }
}