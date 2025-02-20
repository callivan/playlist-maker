package com.example.playlistmaker.search.data.sharedPrefs

import com.example.playlistmaker.search.data.History
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class TracksHistoryRepositoryImpl(private val repository: History) :
    TracksHistoryRepository {

    override fun get(): List<Track> {
        return repository.get().map {
            Track(
                country = it.country,
                trackId = it.trackId,
                trackName = it.trackName,
                previewUrl = it.previewUrl,
                artistName = it.artistName,
                releaseDate = it.releaseDate,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName,
                trackTimeMillis = it.trackTimeMillis,
                primaryGenreName = it.primaryGenreName,
            )
        }
    }

    override fun add(props: Track) {
        repository.add(
            TrackDto(
                country = props.country,
                trackId = props.trackId,
                trackName = props.trackName,
                previewUrl = props.previewUrl,
                artistName = props.artistName,
                releaseDate = props.releaseDate,
                artworkUrl100 = props.artworkUrl100,
                collectionName = props.collectionName,
                trackTimeMillis = props.trackTimeMillis,
                primaryGenreName = props.primaryGenreName,
            )
        )
    }

    override fun clean() {
        repository.clean()
    }
}