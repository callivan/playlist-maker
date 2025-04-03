package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.TracksInteractor
import com.example.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun getTracks(
        text: String
    ): Flow<TracksResponse> {

        return repository.getTracks(text)
    }
}