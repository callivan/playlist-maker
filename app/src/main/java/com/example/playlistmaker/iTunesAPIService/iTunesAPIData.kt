package com.example.playlistmaker.iTunesAPIService

import com.example.playlistmaker.Status

data class iTunesAPITrack(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)

data class iTunesAPIResponse(val resultCount: Int, val results: List<iTunesAPITrack>)

data class iTunesResponse(val status: Status, val tracks: List<iTunesAPITrack>)
