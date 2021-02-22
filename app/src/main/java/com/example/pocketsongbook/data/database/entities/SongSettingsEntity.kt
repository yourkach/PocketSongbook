package com.example.pocketsongbook.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "song_settings",
    foreignKeys = [
        ForeignKey(
            entity = FavoriteSongEntity::class,
            parentColumns = ["url"],
            childColumns = ["song_url"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SongSettingsEntity(
    @PrimaryKey
    val song_url: String,
    val chords_key: Int,
    val text_size: Int
)