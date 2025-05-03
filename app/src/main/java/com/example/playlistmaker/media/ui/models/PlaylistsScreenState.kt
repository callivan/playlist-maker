package com.example.playlistmaker.media.ui.models

sealed interface PlaylistsScreenState {
    object Loading : PlaylistsScreenState

    data class Content(
        val playlists: List<PlaylistUI>
    ) : PlaylistsScreenState

    object Empty : PlaylistsScreenState
}