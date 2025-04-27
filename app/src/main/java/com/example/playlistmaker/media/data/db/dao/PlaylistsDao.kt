package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity

@Dao
interface PlaylistsDao {
    @Query("SELECT * FROM playlist_table ORDER BY timestamp DESC")
    fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun createPlaylist(playlist: PlaylistEntity)

    @Query("UPDATE playlist_table SET tracksId = :tracksId WHERE id = :playlistId")
    fun updatePlaylistTracksId(playlistId: Long, tracksId: List<String>)

    @Transaction
    fun insertTrackIdInPlaylistIfNotExists(playlistId: Long, trackId: String): PlaylistEntity? {
        val playlist = getPlaylistById(playlistId)
        val tracksId = playlist.tracksId

        var state: PlaylistEntity? = null

        if (!tracksId.contains(trackId)) {
            updatePlaylistTracksId(playlistId, tracksId + trackId)

            state = PlaylistEntity(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                tracksId = tracksId,
                img = playlist.img
            )
        }

        return state
    }
}