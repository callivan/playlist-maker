package com.example.playlistmaker.media.domain.api

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getTracks(): Flow<List<Track>>

    fun existsById(id: String): Flow<Boolean>

    fun insertTrack(track: TrackEntity): Flow<Unit>

    fun deleteTrackById(id: String): Flow<Unit>
}