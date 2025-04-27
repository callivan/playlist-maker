package com.example.playlistmaker.media.ui.models


data class PlaylistUI(
    val id: Long,
    val name: String,
    val tracksId: MutableList<String>,
    val description: String? = null,
    val img: String? = null
)