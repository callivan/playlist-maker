package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackDto

interface History {
    fun get(): List<TrackDto>
    fun add(props: TrackDto): Unit
    fun clean(): Unit
}