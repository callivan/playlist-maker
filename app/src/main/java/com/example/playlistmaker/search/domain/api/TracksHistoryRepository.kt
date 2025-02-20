package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TracksHistoryRepository {
    fun get(): List<Track>
    fun add(props: Track): Unit
    fun clean(): Unit
}