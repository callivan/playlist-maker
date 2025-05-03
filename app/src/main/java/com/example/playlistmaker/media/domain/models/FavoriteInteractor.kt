package com.example.playlistmaker.media.domain.models

import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun getFavoriteTracks(): Flow<List<Track>>

    fun existsFavoriteTrackById(id: String): Flow<Boolean>

    fun deleteFavoriteTrackById(id: String): Flow<Unit>

    fun insertFavoriteTrack(track: Track): Flow<Unit>
}