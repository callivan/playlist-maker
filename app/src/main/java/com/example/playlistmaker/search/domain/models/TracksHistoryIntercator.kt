package com.example.playlistmaker.search.domain.models

interface TracksHistoryIntercator {
    fun get(): List<Track>
    fun add(props: Track): Unit
    fun clean(): Unit
}