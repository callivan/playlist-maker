package com.example.playlistmaker.player.domain.models

import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun existsFavoriteTrackById(id: String): Flow<Boolean>

    fun deleteFavoriteTrackById(id: String): Flow<Unit>

    fun insertFavoriteTrack(track: Track): Flow<Unit>

    fun getPlaylists(): Flow<List<Playlist>>

    fun insertPlaylistTrack(playlistId: Long, track: Track): Flow<Playlist?>
}