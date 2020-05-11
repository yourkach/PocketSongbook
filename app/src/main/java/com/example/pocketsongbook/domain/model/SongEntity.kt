package com.example.pocketsongbook.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "songs")
data class SongEntity(
    val artist: String,
    val title: String,
    val lyrics: String,
    @PrimaryKey val link: String,
    @ColumnInfo(name = "time_added") val timeAdded: Long
) {
    constructor(s: Song) : this(
        s.artist,
        s.title,
        s.lyrics,
        s.link,
        System.currentTimeMillis() / 1000
    )
}