package com.example.pocketsongbook.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.domain.SongsWebsite
import com.example.pocketsongbook.domain.models.SongModel

@Entity(tableName = "favorite_songs")
data class FavoriteSongEntity(
    @PrimaryKey val url: String,
    val artist: String,
    val title: String,
    val lyrics: String,
    val website_name: String,
    val time_added: Long
) {

    fun toFavoriteSong(): FavoriteSongModel = FavoriteSongModel(
        song = SongModel(
            artist = artist,
            title = title,
            lyrics = lyrics,
            url = url,
            website = SongsWebsite.valueOf(website_name)
        ),
        timeAdded = time_added
    )

    companion object {
        fun create(songModel: SongModel): FavoriteSongEntity {
            return FavoriteSongEntity(
                artist = songModel.artist,
                title = songModel.title,
                lyrics = songModel.lyrics,
                url = songModel.url,
                time_added = System.currentTimeMillis(),
                website_name = songModel.website.name
            )
        }
    }
}