package com.example.playlistmaker.search.ui.models

sealed class SearchScreenState {
    object Init : SearchScreenState()
    object Pending : SearchScreenState()
    data class Content(
        val tracks: List<TrackUI>,
    ) : SearchScreenState()
    object Empty : SearchScreenState()
    object Error : SearchScreenState()
}