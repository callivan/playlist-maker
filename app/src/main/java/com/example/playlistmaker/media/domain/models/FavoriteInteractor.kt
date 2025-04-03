package com.example.playlistmaker.media.domain.models

import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun getTracks(): Flow<List<Track>>

    fun existsById(id: String): Flow<Boolean>

    fun deleteTrackById(id: String): Flow<Unit>

    fun insertTrack(track: Track): Flow<Unit>
}