package com.example.playlistmaker.playlist_creator.domain.models

import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

interface PlaylistCreatorInteractor {
    fun createPlaylist(playlist: Playlist): Flow<Unit>

    fun insertTrackIdInPlaylistIfNotExists(playlistId: Long, trackId: String): Flow<Playlist?>

    fun savePlaylistImageInStorage(inputStream: InputStream?, filesDir: File?): Flow<File>
}