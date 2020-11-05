package com.example.pocketsongbook.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pocketsongbook.data.models.Song

@Entity(tableName = "songs")
data class SongEntity(
    val artist: String,
    val title: String,
    val lyrics: String,
    @PrimaryKey val url: String,
    @ColumnInfo(name = "time_added") val timeAdded: Long
) {
    constructor(s: Song) : this(
        s.artist,
        s.title,
        s.lyrics,
        s.url,
        System.currentTimeMillis() / 1000
    )
}