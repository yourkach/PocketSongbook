package com.ybond.data_local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "song_options",
    foreignKeys = [
        ForeignKey(
            entity = FavoriteSongEntity::class,
            parentColumns = ["url"],
            childColumns = ["song_url"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SongOptionsEntity(
    @PrimaryKey
    val song_url: String,
    @ColumnInfo(name = "chords_key")
    val chords_key: Int,
    @ColumnInfo(name = "font_size")
    val font_size: Int
)