package com.example.pocketsongbook.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pocketsongbook.data.models.SongModel

@Entity(tableName = "songs")
data class SongEntity(
    val artist: String,
    val title: String,
    val lyrics: String,
    @PrimaryKey val url: String,
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
                artist = songModel.artist,
                title = songModel.title,
                lyrics = songModel.lyrics,
                url = songModel.url,
                timeAdded = System.currentTimeMillis() / 1000
            )
        }
    }
}