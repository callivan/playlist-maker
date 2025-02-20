package com.example.playlistmaker.search.domain.models

interface TracksInteractor {
    fun getTracks(
        text: String,
        onPending: () -> Unit,
        onSuccess: (tracks: List<Track>) -> Unit,
        onError: () -> Unit
    ): Unit
}