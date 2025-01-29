package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TracksResponse

interface TracksRepository {
    fun getTracks(text: String): TracksResponse
}