package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksHistoryIntercator

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository): TracksHistoryIntercator {
    override fun get(): List<Track> {
        return repository.get()
    }

    override fun add(props: Track) {
        repository.add(props)
    }

    override fun clean() {
        repository.clean()
    }
}