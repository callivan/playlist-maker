package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.api.FavoriteRepository
import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository,
) : FavoriteInteractor {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override fun existsFavoriteTrackById(id: String): Flow<Boolean> {
        return favoriteRepository.existsFavoriteTrackById(id)
    }

    override fun insertFavoriteTrack(track: Track): Flow<Unit> {
        return favoriteRepository.insertFavoriteTrack(track)
    }

    override fun deleteFavoriteTrackById(id: String): Flow<Unit> {
        return favoriteRepository.deleteFavoriteTrackById(id)
    }
}