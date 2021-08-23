package com.ybond.core_db_api

import com.ybond.core_db_api.dao.FavoriteSongsDao
import com.ybond.core_db_api.dao.SavedQueriesDao
import com.ybond.core_db_api.dao.SongsOptionsDao

interface DatabaseApi {
    fun savedQueriesDao(): SavedQueriesDao
    fun favoriteSongsDao(): FavoriteSongsDao
    fun songsOptionsDao(): SongsOptionsDao
}