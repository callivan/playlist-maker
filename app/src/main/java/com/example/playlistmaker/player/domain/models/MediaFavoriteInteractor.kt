package com.example.playlistmaker.player.domain.models

import kotlinx.coroutines.flow.Flow

interface MediaFavoriteInteractor {
    fun existsById(id: String): Flow<Boolean>

    fun deleteTrackById(id: String): Flow<Unit>

    fun insertTrack(track: Track): Flow<Unit>
}