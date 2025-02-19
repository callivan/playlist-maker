package com.example.playlistmaker.search.ui.models

sealed class TracksSearchScreenState {
    object Init : TracksSearchScreenState()
    object Pending : TracksSearchScreenState()
    data class Content(
        val tracks: List<TrackUI>,
    ) : TracksSearchScreenState()
    object Empty : TracksSearchScreenState()
    object Error : TracksSearchScreenState()
}