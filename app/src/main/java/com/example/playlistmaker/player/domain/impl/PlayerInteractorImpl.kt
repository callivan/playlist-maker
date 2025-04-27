package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.models.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerPlaylist
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

    override fun getPlaylists(): Flow<List<PlayerPlaylist>> {
        return playlistsInteractor.getPlaylists()
            .map { playlists -> playlists.map { playlist -> convertFromPlaylist(playlist) } }
    }

    override fun insertTrackIdInPlaylistIfNotExists(
        playlistId: Long,
        trackId: String
    ): Flow<PlayerPlaylist?> {
        val playlist = playlistsInteractor.insertTrackIdInPlaylistIfNotExists(playlistId, trackId)
        return playlist.map { p -> if (p != null) convertFromPlaylist(p) else null }
    }

    private fun convertFromTrack(track: Track): com.example.playlistmaker.media.domain.models.Track {
        return com.example.playlistmaker.media.domain.models.Track(
            id = track.id,
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

    private fun convertFromPlaylist(playerPlaylist: Playlist): PlayerPlaylist {
        return PlayerPlaylist(
            id = playerPlaylist.id,
            name = playerPlaylist.name,
            description = playerPlaylist.description,
            tracksId = playerPlaylist.tracksId,
            img = playerPlaylist.img
        )
    }
}