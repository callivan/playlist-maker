package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.api.FavoriteRepository
import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository,
) : FavoriteInteractor {
    override fun getTracks(): Flow<List<Track>> {
        return favoriteRepository.getTracks()
    }

    override fun existsById(id: String): Flow<Boolean> {
        return favoriteRepository.existsById(id)
    }

    override fun insertTrack(track: Track): Flow<Unit> {
        return favoriteRepository.insertTrack(convertFromTrack(track))
    }

    override fun deleteTrackById(id: String): Flow<Unit> {
        return favoriteRepository.deleteTrackById(id)
    }

    private fun convertFromTrack(track: Track): TrackEntity {
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
            primaryGenreName = track.primaryGenreName
        )
    }
}