package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.media.data.converters.DateTypeConverter
import com.example.playlistmaker.media.data.db.dao.FavoriteDao
import com.example.playlistmaker.media.data.db.dao.PlaylistsDao
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistTrackEntity

@Database(
    entities = [FavoriteTrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class, PlaylistTrackCrossRefEntity::class],
    version = 1
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun playlistsDao(): PlaylistsDao
}