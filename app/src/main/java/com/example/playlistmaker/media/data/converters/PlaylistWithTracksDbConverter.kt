package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.PlaylistWithTracksEntity
import com.example.playlistmaker.media.data.dto.PlaylistWithTracksDto
import com.example.playlistmaker.media.domain.models.PlaylistWithTracks

class PlaylistWithTracksDbConverter {
    private val playlistDbConverter = PlaylistDbConverter()
    private val playlistTrackDbConverter = PlaylistTrackDbConverter()

    fun map(playlistWithTracks: PlaylistWithTracksDto): PlaylistWithTracksEntity {
        val tracks = playlistWithTracks.tracks.map { track -> playlistTrackDbConverter.map(track) }

        return PlaylistWithTracksEntity(
            playlist = playlistDbConverter.map(playlistWithTracks.playlist),
            tracks = tracks
        )
    }

    fun map(playlistWithTracks: PlaylistWithTracksEntity): PlaylistWithTracks {
        val tracks = playlistWithTracks.tracks.map { track -> playlistTrackDbConverter.map(track) }

        return PlaylistWithTracks(
            playlist = playlistDbConverter.map(playlistWithTracks.playlist),
            tracks = tracks
        )
    }

    fun map(playlistWithTracks: PlaylistWithTracks): PlaylistWithTracksEntity {
        val tracks = playlistWithTracks.tracks.map { track -> playlistTrackDbConverter.map(track) }

        return PlaylistWithTracksEntity(
            playlist = playlistDbConverter.map(playlistWithTracks.playlist),
            tracks = tracks
        )
    }
}