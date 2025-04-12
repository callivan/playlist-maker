package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.models.MediaFavoriteInteractor
import kotlinx.coroutines.flow.Flow

class MediaFavoriteInteractorImpl(
    private val favoriteInteractor: FavoriteInteractor
) : MediaFavoriteInteractor {

    override fun existsById(id: String): Flow<Boolean> {
        return favoriteInteractor.existsById(id)
    }

    override fun insertTrack(track: Track): Flow<Unit> {
        return favoriteInteractor.insertTrack(convertFromTrack(track))
    }

    override fun deleteTrackById(id: String): Flow<Unit> {
        return favoriteInteractor.deleteTrackById(id)
    }

    private fun convertFromTrack(track: Track): com.example.playlistmaker.media.domain.models.Track {
        return com.example.playlistmaker.media.domain.models.Track(
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