package com.example.playlistmaker.player.domain.models

data class PlayerPlaylist(
    val id: Long,
    val name: String,
    val tracksId: List<String>,
    val description: String? = null,
    val img: String? = null
)