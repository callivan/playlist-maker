package com.example.playlistmaker.search.domain.models

import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun getTracks(text: String): Flow<TracksResponse>
}