package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.media.data.converters.DateTypeConverter
import com.example.playlistmaker.media.data.db.dao.FavoriteDao
import com.example.playlistmaker.media.data.db.entity.TrackEntity

@Database(entities = [TrackEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}