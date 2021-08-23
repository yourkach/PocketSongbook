package com.example.pocketsongbook.data.favorites.impl

import com.example.pocketsongbook.data.favorites.FavoriteModelMapper
import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.data.favorites.FavoriteSongsUrlsDao
import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import com.ybond.core.entities.SongModel
import com.ybond.core_db_api.dao.FavoriteSongsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteSongsRepositoryImpl @Inject constructor(
    private val favoriteSongsDao: FavoriteSongsDao,
    private val favoriteSongsUrlsDao: FavoriteSongsUrlsDao,
    private val modelMapper: FavoriteModelMapper
) : FavouriteSongsRepository {

    override suspend fun getAllFavourites(): List<FavoriteSongModel> {
        return favoriteSongsDao.getAll().map(modelMapper::toModel).also { songs ->
            favoriteSongsUrlsDao.urls.apply {
                clear()
                addAll(songs.map { it.song.url })
            }
        }
    }

    override suspend fun getSongsByQuery(query: String): List<FavoriteSongModel> {
        return favoriteSongsDao.findByQuery(query).map(modelMapper::toModel)
    }

    override suspend fun findSongByUrl(url: String): FavoriteSongModel? {
        return favoriteSongsDao.findByUrl(url).firstOrNull()?.let(modelMapper::toModel)
    }

    override suspend fun removeSongByUrl(url: String) {
        favoriteSongsDao.deleteByUrl(url)
        favoriteSongsUrlsDao.urls.remove(url)
    }

    override suspend fun addSong(song: SongModel) {
        val favoriteSongModel = FavoriteSongModel(
            song = song,
            timeAdded = System.currentTimeMillis()
        )
        favoriteSongsDao.insert(modelMapper.toEntity(favoriteSongModel))
        favoriteSongsUrlsDao.urls.add(song.url)
    }

    override suspend fun containsSong(url: String): Boolean {
        return withContext(Dispatchers.IO) { favoriteSongsUrlsDao.urls.contains(url) }
    }
}

