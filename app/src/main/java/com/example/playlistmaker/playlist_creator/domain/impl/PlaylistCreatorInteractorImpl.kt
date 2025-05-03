package com.example.playlistmaker.playlist_creator.domain.impl

import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.playlist_creator.domain.models.Playlist
import com.example.playlistmaker.playlist_creator.domain.models.PlaylistCreatorInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.InputStream

class PlaylistCreatorInteractorImpl(private val playlistsInteractor: PlaylistsInteractor) :
    PlaylistCreatorInteractor {
    override fun createPlaylist(playlist: Playlist): Flow<Unit> {
        return playlistsInteractor.createPlaylist(convertFromPlaylist(playlist))
    }

    override fun insertTrackIdInPlaylistIfNotExists(
        playlistId: Long,
        trackId: String
    ): Flow<Playlist?> {
        val playlist = playlistsInteractor.insertTrackIdInPlaylistIfNotExists(playlistId, trackId)

        return playlist.map { p -> if (p != null) convertFromPlaylist(p) else null }
    }

    override fun savePlaylistImageInStorage(
        inputStream: InputStream?,
        filesDir: File?
    ): Flow<File> {
        return playlistsInteractor.savePlaylistImageInStorage(inputStream, filesDir)
    }

    private fun convertFromPlaylist(playlist: Playlist): com.example.playlistmaker.media.domain.models.Playlist {
        return com.example.playlistmaker.media.domain.models.Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            tracksId = playlist.tracksId,
            img = playlist.img
        )
    }

    private fun convertFromPlaylist(playlist: com.example.playlistmaker.media.domain.models.Playlist): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            tracksId = playlist.tracksId,
            img = playlist.img
        )
    }
}