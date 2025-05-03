package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.models.PlayerInteractor
import com.example.playlistmaker.player.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayerInteractorImpl(
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : PlayerInteractor {

    override fun existsFavoriteTrackById(id: String): Flow<Boolean> {
        return favoriteInteractor.existsFavoriteTrackById(id)
    }

    override fun insertFavoriteTrack(track: Track): Flow<Unit> {
        return favoriteInteractor.insertFavoriteTrack(convertFromTrack(track))
    }

    override fun deleteFavoriteTrackById(id: String): Flow<Unit> {
        return favoriteInteractor.deleteFavoriteTrackById(id)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsInteractor.getPlaylists()
            .map { playlists -> convertFromPlaylist(playlists) }
    }

    override fun insertPlaylistTrack(playlistId: Long, track: Track): Flow<Playlist?> {
        return playlistsInteractor.insertPlaylistTrack(playlistId, convertFromTrack(track))
            .map { playlist ->
                if (playlist != null) Playlist(
                    id = playlist.id,
                    name = playlist.name,
                    description = playlist.description,
                    tracksCount = playlist.tracksCount,
                    img = playlist.img
                ) else null
            }
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

    private fun convertFromPlaylist(playlists: List<com.example.playlistmaker.media.domain.models.Playlist>): List<Playlist> {
        return playlists.map { playlist ->
            Playlist(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                tracksCount = playlist.tracksCount,
                img = playlist.img
            )
        }
    }
}