package com.example.playlistmaker.media.domain.api

import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteTracks(): Flow<List<Track>>

    fun existsFavoriteTrackById(id: String): Flow<Boolean>

    fun insertFavoriteTrack(track: Track): Flow<Unit>

    fun deleteFavoriteTrackById(id: String): Flow<Unit>
}