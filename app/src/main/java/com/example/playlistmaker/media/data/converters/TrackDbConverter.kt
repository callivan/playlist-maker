package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.data.dto.TrackDto
import com.example.playlistmaker.media.domain.models.Track

class TrackDbConverter {
    fun map(track: TrackDto): FavoriteTrackEntity {
        return FavoriteTrackEntity(
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

    fun map(track: FavoriteTrackEntity): Track {
        return Track(
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

    fun map(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
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