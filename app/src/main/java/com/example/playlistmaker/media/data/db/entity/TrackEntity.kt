package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.material.internal.ParcelableSparseBooleanArray
import java.util.Date

@Entity(tableName = "favorite_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val trackId: String,
    val previewUrl: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String,
)