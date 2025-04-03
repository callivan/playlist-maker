package com.example.playlistmaker.media.ui.models

sealed interface FavoriteScreenState {
    object Loading : FavoriteScreenState

    data class Content(
        val tracks: List<TrackUI>
    ) : FavoriteScreenState

    object Empty : FavoriteScreenState
}