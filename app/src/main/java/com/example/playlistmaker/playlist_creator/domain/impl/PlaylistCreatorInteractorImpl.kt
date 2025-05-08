package com.example.playlistmaker.playlist_creator.domain.impl

import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.playlist_creator.domain.models.Playlist
import com.example.playlistmaker.playlist_creator.domain.models.PlaylistCreatorInteractor
import com.example.playlistmaker.playlist_creator.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.InputStream

class PlaylistCreatorInteractorImpl(private val playlistsInteractor: PlaylistsInteractor) :
    PlaylistCreatorInteractor {

    override fun insertPlaylist(playlist: Playlist): Flow<Playlist> {
        return playlistsInteractor.insertPlaylist(convertFromPlaylist(playlist)).map { playlist ->
            convertFromPlaylist(playlist)
        }
    }

    override fun insertPlaylistTrack(playlistId: Long, track: Track): Flow<Playlist?> {
        return playlistsInteractor.insertPlaylistTrack(playlistId, convertFromTrack(track))
            .map { playlist ->
                if (playlist != null) convertFromPlaylist(playlist) else null
            }
    }

    override fun savePlaylistImageInStorage(
        inputStream: InputStream?, filesDir: File?
    ): Flow<File> {
        return playlistsInteractor.savePlaylistImageInStorage(inputStream, filesDir)
    }

    private fun convertFromPlaylist(playlist: Playlist): com.example.playlistmaker.media.domain.models.Playlist {
        return com.example.playlistmaker.media.domain.models.Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            tracksCount = playlist.tracksCount,
            img = playlist.img
        )
    }

    private fun convertFromPlaylist(playlist: com.example.playlistmaker.media.domain.models.Playlist): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            tracksCount = playlist.tracksCount,
            img = playlist.img
        )
    }

    private fun convertFromTrack(track: Track): com.example.playlistmaker.media.domain.models.Track {
        return com.example.playlistmaker.media.domain.models.Track(
            country = track.country,
            trackId = track.trackId,
            trackName = track.trackName,
            previewUrl = track.previewUrl,
            artistName = track.artistName,
            releaseDate = track.releaseDate,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            trackTimeMillis = track.trackTimeMillis,
            primaryGenreName = track.primaryGenreName,
        )
    }
}