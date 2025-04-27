package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.data.dto.TrackDto
import com.example.playlistmaker.media.domain.models.Track

class TrackDbConverter {
    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
            id = track.id,
            country = track.country,
            trackId = track.trackId,
            trackName = track.trackName,
            previewUrl = track.previewUrl,
            artistName = track.artistName,
            releaseDate = track.releaseDate,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            trackTimeMillis = track.trackTimeMillis,
            primaryGenreName = track.primaryGenreName,
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            id = track.id,
            country = track.country,
            trackId = track.trackId,
            trackName = track.trackName,
            previewUrl = track.previewUrl,
            artistName = track.artistName,
            releaseDate = track.releaseDate,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            trackTimeMillis = track.trackTimeMillis,
            primaryGenreName = track.primaryGenreName,
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            id = track.id,
            country = track.country,
            trackId = track.trackId,
            trackName = track.trackName,
            previewUrl = track.previewUrl,
            artistName = track.artistName,
            releaseDate = track.releaseDate,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            trackTimeMillis = track.trackTimeMillis,
            primaryGenreName = track.primaryGenreName,
        )
    }
}