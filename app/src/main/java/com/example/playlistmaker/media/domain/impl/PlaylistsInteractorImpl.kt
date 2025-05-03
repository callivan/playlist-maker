package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.api.PlaylistsRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.PlaylistWithTracks
import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.InputStream

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository,
) : PlaylistsInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks> {
        return playlistsRepository.getPlaylistWithTracks(playlistId)
    }

    override fun insertPlaylist(playlist: Playlist): Flow<Playlist> {
        return playlistsRepository.insertPlaylist(playlist)
    }

    override fun insertPlaylistTrack(playlistId: Long, track: Track): Flow<Playlist?> {
        return playlistsRepository.insertPlaylistTrack(playlistId, track)
    }

    override fun deletePlaylist(playlistId: Long) {
        playlistsRepository.deletePlaylist(playlistId)
    }

    override fun deletePlaylistTrack(playlistId: Long, trackId: String) {
        playlistsRepository.deletePlaylistTrack(playlistId, trackId)
    }

    override fun savePlaylistImageInStorage(
        inputStream: InputStream?,
        filesDir: File?
    ): Flow<File> {
        return playlistsRepository.savePlaylistImageInStorage(inputStream, filesDir)
    }
}