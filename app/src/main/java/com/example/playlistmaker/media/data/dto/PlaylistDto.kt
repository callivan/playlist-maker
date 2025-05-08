package com.example.playlistmaker.media.data.dto


data class PlaylistDto(
    val id: Long,
    val name: String,
    val tracksCount: Int,
    val description: String? = null,
    val img: String? = null
)