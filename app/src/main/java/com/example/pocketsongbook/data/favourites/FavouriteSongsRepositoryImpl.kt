package com.example.pocketsongbook.data.favourites

import com.chibatching.kotpref.KotprefModel
import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.database.entities.SongEntity
import com.example.pocketsongbook.data.models.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: 14.02.21 вынести префсы в отдельный класс
class FavouriteSongsRepositoryImpl(
    private val favouriteSongsDao: FavouriteSongsDao
) : KotprefModel(), FavouriteSongsRepository {

    private val favouriteSongUrls by stringSetPref()

    override suspend fun getAllFavourites(): List<SongEntity> {
        return withContext(Dispatchers.IO) {
            val songs = favouriteSongsDao.getAll()
            favouriteSongUrls.clear()
            favouriteSongUrls.addAll(songs.map { it.url })
            songs
        }
    }

    override suspend fun getSongsByQuery(query: String): List<SongEntity> {
        return withContext(Dispatchers.IO) { favouriteSongsDao.findByName(query) }
    }

    override suspend fun findSongByUrl(url: String): SongEntity? {
        return withContext(Dispatchers.IO) { favouriteSongsDao.findByUrl(url).firstOrNull() }
    }

    override suspend fun removeSongByUrl(url: String) {
        withContext(Dispatchers.IO) {
            favouriteSongsDao.deleteByUrl(url)
            favouriteSongUrls.remove(url)
        }
    }

    override suspend fun addSong(song: SongModel) {
        withContext(Dispatchers.IO) {
            favouriteSongsDao.insert(SongEntity.create(songModel = song))
            favouriteSongUrls.add(song.url)
        }
    }

    override suspend fun removeSong(song: SongModel) {
        withContext(Dispatchers.IO) {
            favouriteSongsDao.deleteByUrl(song.url)
            favouriteSongUrls.remove(song.url)
        }
    }

    override suspend fun containsSong(url: String): Boolean {
        return withContext(Dispatchers.IO) { favouriteSongUrls.contains(url) }
    }
}