package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.TrackEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_table ORDER BY timestamp DESC")
    fun getTracks(): List<TrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_table WHERE trackId = :id)")
    fun existsById(id: String): Boolean

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM favorite_table WHERE trackId = :id")
    fun deleteTrackById(id: String)
}