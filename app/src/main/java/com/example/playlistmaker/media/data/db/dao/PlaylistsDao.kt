package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistWithTracksEntity

@Dao
interface PlaylistsDao {
    @Query("SELECT * FROM playlistId_trackId_table WHERE playlistId = :playlistId AND trackId = :trackId")
    fun getPlaylistTrackCrossRef(playlistId: Long, trackId: String): PlaylistTrackCrossRefEntity?

    @Query("SELECT COUNT(*) FROM playlistId_trackId_table WHERE trackId = :trackId")
    fun getTrackCrossRefCount(trackId: String): Int

    @Query("SELECT * FROM playlist_table ORDER BY timestamp DESC")
    fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Transaction
    @Query(
        """SELECT  playlist_track_table.*, playlistId_trackId_table.timestamp
              FROM playlist_track_table
              INNER JOIN playlistId_trackId_table ON playlist_track_table.trackId = playlistId_trackId_table.trackId
              WHERE playlistId_trackId_table.playlistId = :playlistId
              ORDER BY playlistId_trackId_table.timestamp DESC
    """
    )
    fun getPlaylistTracks(playlistId: Long): List<PlaylistTrackEntity>

    @Transaction
    fun getPlaylistWithTracks(playlistId: Long): PlaylistWithTracksEntity {
        val playlist = getPlaylistById(playlistId)
        val tracks = getPlaylistTracks(playlistId)

        return PlaylistWithTracksEntity(playlist = playlist, tracks = tracks)
    }

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistAndReturnId(playlist: PlaylistEntity): Long

    @Transaction
    fun insertPlaylist(playlist: PlaylistEntity): PlaylistEntity {
        val id = insertPlaylistAndReturnId(playlist)

        return getPlaylistById(id)
    }

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: PlaylistTrackEntity)

    @Query("UPDATE playlist_table SET tracksCount = :tracksCount WHERE id = :playlistId")
    fun updatePlaylistTracksCount(playlistId: Long, tracksCount: Int)

    @Insert(entity = PlaylistTrackCrossRefEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertPlaylistTrackCrossRef(crossRefs: PlaylistTrackCrossRefEntity)

    @Transaction
    fun insertPlaylistTrack(playlistId: Long, track: PlaylistTrackEntity): PlaylistEntity? {
        val crossRef = getPlaylistTrackCrossRef(playlistId, track.trackId)

        if (crossRef == null) {
            insertTrack(track)

            insertPlaylistTrackCrossRef(
                PlaylistTrackCrossRefEntity(playlistId, track.trackId)
            )

            val tracksCount = getPlaylistWithTracks(playlistId).tracks.size

            updatePlaylistTracksCount(playlistId, tracksCount)

            return getPlaylistById(playlistId)
        }

        return null
    }

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    fun deletePlaylistById(playlistId: Long)

    @Query("DELETE FROM playlistId_trackId_table WHERE playlistId = :playlistId")
    fun deletePlaylistCrossRef(playlistId: Long)

    @Transaction
    fun deletePlaylist(playlistId: Long) {
        val playlist = getPlaylistWithTracks(playlistId)

        playlist.tracks.forEach {
            deletePlaylistTrack(playlistId, it.trackId)
        }

        deletePlaylistCrossRef(playlistId)
        deletePlaylistById(playlistId)
    }

    @Query("DELETE FROM playlist_track_table WHERE trackId = :trackId")
    fun deletePlaylistTrackById(trackId: String)

    @Query("DELETE FROM playlistId_trackId_table WHERE playlistId = :playlistId AND trackId = :trackId")
    fun deletePlaylistTrackCrossRef(playlistId: Long, trackId: String)

    @Transaction
    fun deletePlaylistTrack(playlistId: Long, trackId: String) {
        deletePlaylistTrackCrossRef(playlistId, trackId)

        val crossRefCount = getTrackCrossRefCount(trackId)

        if (crossRefCount == 0) {
            deletePlaylistTrackById(trackId)
        }

        val tracksCount = getPlaylistWithTracks(playlistId).tracks.size

        updatePlaylistTracksCount(playlistId, tracksCount)
    }
}
