package com.example.pocketsongbook.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_songs")
data class FavoriteSongEntity(
    @PrimaryKey val url: String,
    val artist: String,
    val title: String,
    val lyrics: String,
    val website_name: String,
    val time_added: Long
)