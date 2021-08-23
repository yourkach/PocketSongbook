package com.ybond.feature_songview_api

import com.github.terrakok.cicerone.Router
import com.ybond.core.entities.SongModel

interface SongViewStarter {

    fun Router.openSongView(songModel: SongModel)

}