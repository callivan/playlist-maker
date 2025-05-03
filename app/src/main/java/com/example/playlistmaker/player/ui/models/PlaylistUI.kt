package com.example.playlistmaker.player.ui.models


data class PlaylistUI(
    val id: Long,
    val name: String,
    val tracksCount: Int = 0,
    val description: String? = null,
    val img: String? = null
)