package com.example.playlistmaker.media.ui.models


data class PlaylistUI(
    val id: Long,
    val name: String,
    val tracksCount: Int,
    val description: String? = null,
    val img: String? = null
)