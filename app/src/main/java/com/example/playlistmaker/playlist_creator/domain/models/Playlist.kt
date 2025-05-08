package com.example.playlistmaker.playlist_creator.domain.models

data class Playlist(
    val id: Long,
    val name: String,
    val tracksCount: Int,
    val description: String? = null,
    val img: String? = null
)