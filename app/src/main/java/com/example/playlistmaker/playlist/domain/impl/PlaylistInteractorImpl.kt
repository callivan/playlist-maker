package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.playlist.domain.models.Playlist
import com.example.playlistmaker.playlist.domain.models.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.example.playlistmaker.playlist.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.Long
import kotlin.String

class PlaylistInteractorImpl(
    private val playlistsInteractor: PlaylistsInteractor,
) : PlaylistInteractor {

    override fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks> {
        return playlistsInteractor.getPlaylistWithTracks(playlistId).map { state ->
            val playlist = Playlist(
                id = state.playlist.id,
                name = state.playlist.name,
                tracksCount = state.playlist.tracksCount,
                description = state.playlist.description,
                img = state.playlist.img
            )

            val tracks = state.tracks.map { track ->
                Track(
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


            PlaylistWithTracks(
                playlist = playlist,
                tracks = tracks
            )
        }
    }

    override fun deletePlaylistTrack(playlistId: Long, trackId: String) {
        return playlistsInteractor.deletePlaylistTrack(playlistId, trackId)
    }

    override fun deletePlaylist(playlistId: Long) {
        return playlistsInteractor.deletePlaylist(playlistId)
    }
}