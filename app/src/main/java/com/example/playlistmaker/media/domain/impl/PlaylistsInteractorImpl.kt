package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.api.PlaylistsRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository,
) : PlaylistsInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override fun createPlaylist(playlist: Playlist): Flow<Unit> {
        return playlistsRepository.createPlaylist(playlist)
    }

    override fun insertTrackIdInPlaylistIfNotExists(
        playlistId: Long,
        trackId: String
    ): Flow<Playlist?> {
        return playlistsRepository.insertTrackIdInPlaylistIfNotExists(playlistId, trackId)
    }

    override fun savePlaylistImageInStorage(
        inputStream: InputStream?,
        filesDir: File?
    ): Flow<File> {
        return playlistsRepository.savePlaylistImageInStorage(inputStream, filesDir)
    }
}