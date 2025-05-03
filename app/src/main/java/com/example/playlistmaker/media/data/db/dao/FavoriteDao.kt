package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_table ORDER BY timestamp DESC")
    fun getFavoriteTracks(): List<FavoriteTrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_table WHERE trackId = :id)")
    fun existsFavoriteTrackById(id: String): Boolean

    @Insert(entity = FavoriteTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteTrack(track: FavoriteTrackEntity)

    @Query("DELETE FROM favorite_table WHERE trackId = :id")
    fun deleteFavoriteTrackById(id: String)
}