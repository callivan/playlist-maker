package com.example.playlistmaker.media.domain.models

import java.util.Date

data class Track(
    val previewUrl: String,
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String
)
