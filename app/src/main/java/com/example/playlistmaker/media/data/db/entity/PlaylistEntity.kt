package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val name: String,
    val tracksId: MutableList<String> = mutableListOf(),
    val description: String? = null,
    val img: String? = null
)