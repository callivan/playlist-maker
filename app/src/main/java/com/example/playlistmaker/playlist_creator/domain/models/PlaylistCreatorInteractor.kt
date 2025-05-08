package com.example.playlistmaker.playlist_creator.domain.models

import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

interface PlaylistCreatorInteractor {
    fun insertPlaylist(playlist: Playlist): Flow<Playlist>

    fun insertPlaylistTrack(playlistId: Long, track: Track): Flow<Playlist?>

    fun savePlaylistImageInStorage(inputStream: InputStream?, filesDir: File?): Flow<File>
}