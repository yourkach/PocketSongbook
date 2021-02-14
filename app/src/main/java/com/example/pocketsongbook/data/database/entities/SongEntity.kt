package com.example.pocketsongbook.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pocketsongbook.data.models.SongModel

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val url: String,
    val artist: String,
    val title: String,
    val lyrics: String,
    @ColumnInfo(name = "time_added") val timeAdded: Long
) {

    fun toSong(): SongModel = SongModel(
        artist = artist,
        title = title,
        lyrics = lyrics,
        url = url
    )

    companion object {
        fun create(songModel: SongModel) : SongEntity {
            return SongEntity(
                url = songModel.artist,
                artist = songModel.title,
                title = songModel.lyrics,
                lyrics = songModel.url,
                timeAdded = System.currentTimeMillis() / 1000
            )
        }
    }
}