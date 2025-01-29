package com.example.playlistmaker.data.sharedPreferences

import android.content.SharedPreferences
import com.example.playlistmaker.data.ISharedPreferences
import com.example.playlistmaker.presentation.models.TrackUI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val SEARCH_HISTORY = "search_history"

private val history: MutableList<TrackUI> = mutableListOf()

class TracksHistorySharedPreferences(
    private val sharedPrefs: SharedPreferences,
    private val onChanged: () -> Unit
) : ISharedPreferences<List<TrackUI>, TrackUI> {
    init {
        history.clear()

        val json = sharedPrefs.getString(
            SEARCH_HISTORY,
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
            .putString(SEARCH_HISTORY, json).apply()

        onChanged()
    }

    override fun get(): List<TrackUI> {
        return history
    }

    override fun clean() {
        history.clear()
        sharedPrefs.edit().remove(SEARCH_HISTORY).apply()

        onChanged()
    }
}