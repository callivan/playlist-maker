package com.example.playlistmaker.media.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    primaryKeys = ["playlistId", "trackId"],
    tableName = "playlistId_trackId_table",
)
data class PlaylistTrackCrossRefEntity(
    val playlistId: Long,
    val trackId: String
)

data class PlaylistWithTracksEntity(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trackId",
        associateBy = Junction(
            value = PlaylistTrackCrossRefEntity::class,
            parentColumn = "playlistId",
            entityColumn = "trackId"
        )
    )
    val tracks: List<PlaylistTrackEntity>
)