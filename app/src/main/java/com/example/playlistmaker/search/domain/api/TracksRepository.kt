package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun getTracks(text: String): Flow<TracksResponse>
}