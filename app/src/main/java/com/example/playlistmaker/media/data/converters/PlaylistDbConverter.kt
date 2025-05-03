package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: PlaylistDto): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            tracksId = playlist.tracksId,
            description = playlist.description,
            img = playlist.img
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            tracksId = playlist.tracksId,
            description = playlist.description,
            img = playlist.img
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            tracksId = playlist.tracksId,
            description = playlist.description,
            img = playlist.img
        )
    }
}