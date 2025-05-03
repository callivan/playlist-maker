package com.example.playlistmaker.playlist_creator.ui.models

sealed interface PlaylistCreatorScreenState {
    data class Init(val playlist: PlaylistUI) : PlaylistCreatorScreenState

    data class ChangeData(val playlist: PlaylistUI) : PlaylistCreatorScreenState

    data class Created(val playlists: PlaylistUI) : PlaylistCreatorScreenState
}