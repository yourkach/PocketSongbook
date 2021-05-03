package com.example.pocketsongbook.data.favorites.impl

import com.example.pocketsongbook.data.database.FavouriteSongsDao
import com.example.pocketsongbook.data.favorites.FavoriteModelMapper
import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.data.favorites.FavoriteSongsUrlsDao
import com.example.pocketsongbook.domain.favorites.FavouriteSongsRepository
import com.example.pocketsongbook.domain.models.SongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import javax.inject.Singleton

@Singleton
@InjectConstructor
class FavouriteSongsRepositoryImpl(
    private val favouriteSongsDao: FavouriteSongsDao,
    private val favoriteSongsUrlsDao: FavoriteSongsUrlsDao,
    private val modelMapper: FavoriteModelMapper
) : FavouriteSongsRepository {

    override suspend fun getAllFavourites(): List<FavoriteSongModel> {
        return favouriteSongsDao.getAll().map(modelMapper::toModel).also { songs ->
            favoriteSongsUrlsDao.urls.apply {
                clear()
                addAll(songs.map { it.song.url })
            }
        }
    }

    override suspend fun getSongsByQuery(query: String): List<FavoriteSongModel> {
        return favouriteSongsDao.findByQuery(query).map(modelMapper::toModel)
    }

    override suspend fun findSongByUrl(url: String): FavoriteSongModel? {
        return favouriteSongsDao.findByUrl(url).firstOrNull()?.let(modelMapper::toModel)
    }

    override suspend fun removeSongByUrl(url: String) {
        favouriteSongsDao.deleteByUrl(url)
        favoriteSongsUrlsDao.urls.remove(url)
    }

    override suspend fun addSong(song: SongModel) {
        val favoriteSongModel = FavoriteSongModel(
            song = song,
            timeAdded = System.currentTimeMillis()
        )
        favouriteSongsDao.insert(modelMapper.toEntity(favoriteSongModel))
        favoriteSongsUrlsDao.urls.add(song.url)
    }

    override suspend fun containsSong(url: String): Boolean {
        return withContext(Dispatchers.IO) { favoriteSongsUrlsDao.urls.contains(url) }
    }
}

