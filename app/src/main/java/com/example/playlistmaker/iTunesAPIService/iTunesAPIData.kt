package com.example.playlistmaker.iTunesAPIService

import com.example.playlistmaker.Status
import java.util.Date

data class iTunesAPITrack(
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

data class iTunesAPIResponse(val resultCount: Int, val results: List<iTunesAPITrack>)

data class iTunesResponse(val status: Status, val tracks: List<iTunesAPITrack>)
