package com.example.playlistmaker.playlist.ui.models

sealed interface PlaylistScreenState {
    object Loading : PlaylistScreenState

    data class Content(
        val state: PlaylistWithTracksUI
    ) : PlaylistScreenState

    object Empty : PlaylistScreenState
}