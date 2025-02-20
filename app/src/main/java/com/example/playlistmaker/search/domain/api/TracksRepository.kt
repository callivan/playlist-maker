package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TracksResponse

interface TracksRepository {
    fun getTracks(text: String): TracksResponse
}