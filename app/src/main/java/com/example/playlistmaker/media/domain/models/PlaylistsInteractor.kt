package com.example.playlistmaker.media.domain.models

import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>

    fun insertPlaylist(playlist: Playlist): Flow<Playlist>

    fun insertPlaylistTrack(playlistId: Long, track: Track): Flow<Playlist?>

    fun deletePlaylist(playlistId: Long)

    fun deletePlaylistTrack(playlistId: Long, trackId: String)

    fun savePlaylistImageInStorage(inputStream: InputStream?, filesDir: File?): Flow<File>
}