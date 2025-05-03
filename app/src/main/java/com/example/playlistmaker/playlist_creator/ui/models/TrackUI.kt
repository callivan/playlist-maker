package com.example.playlistmaker.playlist_creator.ui.models

import java.util.Date

data class TrackUI(
    val previewUrl: String = "",
    val trackId: String = "",
    val trackName: String = "",
    val artistName: String = "",
    val trackTimeMillis: Long = 0L,
    val artworkUrl100: String = "",
    val collectionName: String? = null,
    val releaseDate: Date = Date(),
    val primaryGenreName: String = "",
    val country: String = "",
)