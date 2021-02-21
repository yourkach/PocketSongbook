package com.example.pocketsongbook.data.favorites.impl

import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.database.entities.FavoriteSongEntity
import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.data.favorites.FavoriteSongsUrlsDao
import com.example.pocketsongbook.data.models.SongModel
import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: 14.02.21 вынести префсы в отдельный класс
class FavouriteSongsRepositoryImpl(
    private val favouriteSongsDao: FavouriteSongsDao,
    private val favoriteSongsUrlsDao: FavoriteSongsUrlsDao
) : FavouriteSongsRepository {

    override suspend fun getAllFavourites(): List<FavoriteSongModel> {
        return withContext(Dispatchers.IO) {
            favouriteSongsDao.getAll().map {
                it.toFavoriteSong()
            }.also { songs ->
                favoriteSongsUrlsDao.urls.apply {
                    clear()
                    addAll(songs.map { it.song.url })
                }
            }
        }
    }

    override suspend fun getSongsByQuery(query: String): List<FavoriteSongModel> {
        return withContext(Dispatchers.IO) {
            favouriteSongsDao.findByName(query).map(FavoriteSongEntity::toFavoriteSong)
        }
    }

    override suspend fun findSongByUrl(url: String): FavoriteSongModel? {
        return withContext(Dispatchers.IO) {
            favouriteSongsDao.findByUrl(url).firstOrNull()?.toFavoriteSong()
        }
    }

    override suspend fun removeSongByUrl(url: String) {
        withContext(Dispatchers.IO) {
            favouriteSongsDao.deleteByUrl(url)
            favoriteSongsUrlsDao.urls.remove(url)
        }
    }

    override suspend fun addSong(song: SongModel) {
        withContext(Dispatchers.IO) {
            favouriteSongsDao.insert(FavoriteSongEntity.create(songModel = song))
            favoriteSongsUrlsDao.urls.add(song.url)
        }
    }

    override suspend fun containsSong(url: String): Boolean {
        return withContext(Dispatchers.IO) { favoriteSongsUrlsDao.urls.contains(url) }
    }
}

