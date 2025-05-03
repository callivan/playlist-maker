package com.example.playlistmaker.player.ui.models

sealed interface BottomSheetScreenState {
    object Loading : BottomSheetScreenState

    data class Content(
        val playlists: List<PlaylistUI>
    ) : BottomSheetScreenState

    object Empty : BottomSheetScreenState

    data class Update(val playlist: PlaylistUI, val state: Boolean) : BottomSheetScreenState
}