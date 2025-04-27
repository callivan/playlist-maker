package com.example.playlistmaker.media.domain.models

data class Playlist(
    val id: Long,
    val name: String,
    val tracksId: MutableList<String>,
    val description: String? = null,
    val img: String? = null
)