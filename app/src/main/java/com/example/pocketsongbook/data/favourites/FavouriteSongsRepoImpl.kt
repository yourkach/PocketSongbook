package com.example.pocketsongbook.data.favourites

import com.chibatching.kotpref.KotprefModel
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.database.entities.SongEntity
import com.example.pocketsongbook.data.models.SongModel


class FavouriteSongsRepoImpl(
    private val favouriteSongsDao: FavouriteSongsDao
) : KotprefModel(), FavouriteSongsRepo {

    private val favouriteSongUrls by stringSetPref()

    override suspend fun getAllFavourites(): List<SongEntity> {
        val songs = favouriteSongsDao.getAll()
        favouriteSongUrls.clear()
        favouriteSongUrls.addAll(songs.map { it.url })
        return songs
    }

    override suspend fun getSongsByQuery(query: String): List<SongEntity> {
        return favouriteSongsDao.findByName(query)
    }

    override suspend fun findSongByUrl(url: String): SongEntity? {
        return favouriteSongsDao.findByUrl(url).firstOrNull()
    }

    override suspend fun removeSongByUrl(url: String) {
        favouriteSongsDao.deleteByUrl(url)
        favouriteSongUrls.remove(url)
    }

    override suspend fun addSong(song: SongModel) {
        favouriteSongsDao.insert(SongEntity.create(songModel = song))
        favouriteSongUrls.add(song.url)
    }

    override suspend fun removeSong(song: SongModel) {
        favouriteSongsDao.deleteByUrl(song.url)
        favouriteSongUrls.remove(song.url)
    }

    override suspend fun containsSong(url: String): Boolean {
        return favouriteSongUrls.contains(url)
    }
}