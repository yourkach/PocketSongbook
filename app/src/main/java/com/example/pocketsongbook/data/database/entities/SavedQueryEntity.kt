package com.example.pocketsongbook.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_search_queries")
data class SavedQueryEntity(
    @PrimaryKey
    val text: String,
    val saved_at: Long
)