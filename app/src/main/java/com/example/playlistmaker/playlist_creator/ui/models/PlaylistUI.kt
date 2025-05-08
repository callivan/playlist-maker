package com.example.playlistmaker.playlist_creator.ui.models


data class PlaylistUI(
    val id: Long = 0L,
    var name: String = "",
    var tracksCount: Int = 0,
    var description: String? = null,
    var img: String? = null
)