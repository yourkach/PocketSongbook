package com.example.pocketsongbook.data.favourites

import com.chibatching.kotpref.KotprefModel
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.database.SongEntity
import com.example.pocketsongbook.data.models.Song


class FavouriteSongsRepoImpl(
    private val favouriteSongsDao: FavouriteSongsDao
) : KotprefModel(), FavouriteSongsRepo {

    private val favouriteSongUrls by stringSetPref()

    override suspend fun getAllFavourites(): List<Song> {
        val songs = favouriteSongsDao.getAll().map(::Song)
        favouriteSongUrls.clear()
        favouriteSongUrls.addAll(songs.map { it.url })
        return songs
    }

    override suspend fun getSongsByQuery(query: String): List<Song> {
        return favouriteSongsDao.findByName(query).map(::Song)
    }

    override suspend fun addSong(song: Song) {
        favouriteSongsDao.insert(SongEntity(song))
        favouriteSongUrls.add(song.url)
    }

    override suspend fun removeSong(song: Song) {
        favouriteSongsDao.deleteByUrl(song.url)
        favouriteSongUrls.remove(song.url)
    }

    override suspend fun containsSong(url: String): Boolean {
        return favouriteSongUrls.contains(url)
    }
}