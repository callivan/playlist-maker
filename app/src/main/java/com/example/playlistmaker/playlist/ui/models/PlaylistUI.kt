package com.example.playlistmaker.playlist.ui.models


data class PlaylistUI(
    val id: Long = 0L,
    val name: String = "",
    val tracksCount: Int = 0,
    val description: String? = null,
    val img: String? = null
)