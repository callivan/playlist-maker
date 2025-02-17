package com.example.playlistmaker.search.data.sharedPrefs

import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.search.data.SharedPrefs
import com.example.playlistmaker.search.ui.models.TrackUI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val history: MutableList<TrackUI> = mutableListOf()

class TracksHistorySharedPrefs(
    private val sharedPrefs: android.content.SharedPreferences,
) : SharedPrefs<List<TrackUI>, TrackUI> {
    init {
        history.clear()

        val json = sharedPrefs.getString(
            Const.SEARCH_HISTORY,
            null
        )
        val type = object : TypeToken<MutableList<TrackUI>>() {}.type
        val list = Gson().fromJson<MutableList<TrackUI>>(json, type)

        if (list != null && list.isNotEmpty()) {
            history.addAll(list)
        }
    }

    override fun add(track: TrackUI) {
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

    override fun get(): List<TrackUI> {
        return history
    }

    override fun clean() {
        history.clear()
        sharedPrefs.edit().remove(Const.SEARCH_HISTORY).apply()
    }
}