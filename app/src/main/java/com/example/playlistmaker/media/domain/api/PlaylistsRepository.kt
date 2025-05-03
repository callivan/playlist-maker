package com.example.playlistmaker.media.domain.api

import com.example.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

interface PlaylistsRepository {
    fun getPlaylists(): Flow<List<Playlist>>

    fun createPlaylist(playlist: Playlist): Flow<Unit>

    fun insertTrackIdInPlaylistIfNotExists(playlistId: Long, trackId: String): Flow<Playlist?>

    fun savePlaylistImageInStorage(inputStream: InputStream?, filesDir: File?): Flow<File>
}