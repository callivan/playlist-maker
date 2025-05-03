package com.example.playlistmaker.media.data.dto


data class PlaylistDto(
    val id: Long,
    val name: String,
    val tracksId: MutableList<String> = mutableListOf(),
    val description: String? = null,
    val img: String? = null
)