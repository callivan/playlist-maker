package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorite_table")
data class FavoriteTrackEntity(
    @PrimaryKey
    val trackId: String,
    val timestamp: Long = System.currentTimeMillis(),
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

@Entity(tableName = "playlist_track_table")
data class PlaylistTrackEntity(
    @PrimaryKey
    val trackId: String,
    val timestamp: Long = System.currentTimeMillis(),
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