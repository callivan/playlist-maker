package com.example.playlistmaker.playlist.domain.models

import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>

    fun deletePlaylistTrack(playlistId: Long, trackId: String)

    fun deletePlaylist(playlistId: Long)
}