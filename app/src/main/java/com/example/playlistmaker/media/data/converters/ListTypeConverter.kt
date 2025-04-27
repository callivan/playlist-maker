package com.example.playlistmaker.media.data.converters

import androidx.room.TypeConverter

class ListTypeConverter {
    @TypeConverter
    fun listToString(list: MutableList<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun stringToList(str: String?): MutableList<String>? {
        return str?.split(",")?.map { it.trim() }
            ?.filter { it.isNotEmpty() } as MutableList<String>?
    }
}