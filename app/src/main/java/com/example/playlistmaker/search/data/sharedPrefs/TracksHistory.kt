package com.example.playlistmaker.search.data.sharedPrefs

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.search.data.History
import com.example.playlistmaker.search.data.dto.TrackDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val history: MutableList<TrackDto> = mutableListOf()

class TracksHistory(
    private val context: Application
) : History {

    private val sharedPrefs = context.getSharedPreferences(Const.SEARCH_HISTORY_PREFS, MODE_PRIVATE)

    init {
        history.clear()

        val json = sharedPrefs.getString(
            Const.SEARCH_HISTORY,
            null
        )
        val type = object : TypeToken<MutableList<TrackDto>>() {}.type
        val list = Gson().fromJson<MutableList<TrackDto>>(json, type)

        if (list != null && list.isNotEmpty()) {
            history.addAll(list)
        }
    }

    override fun add(track: TrackDto) {
        var index = -1
        var isContains = false

        for ((i, t) in history.withIndex()) {
            isContains = t.trackId == track.trackId
            index = if (isContains) i else -1

            if (isContains) break
        }

        if (isContains) {
            history.removeAt(index)
        }

        if (history.size == 10) {
            history.removeAt(history.lastIndex)
        }

        history.add(0, track)

        val json = Gson().toJson(history)

        sharedPrefs.edit()
            .putString(Const.SEARCH_HISTORY, json).apply()
    }

    override fun get(): List<TrackDto> {
        return history
    }

    override fun clean() {
        history.clear()
        sharedPrefs.edit().remove(Const.SEARCH_HISTORY).apply()
    }
}