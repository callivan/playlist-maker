package com.example.playlistmaker.searchHistory

import android.content.SharedPreferences
import com.example.playlistmaker.iTunesAPIService.iTunesAPITrack
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val SEARCH_HISTORY = "search_history"

private val searchHistoryList: MutableList<iTunesAPITrack> = mutableListOf()

class SearchHistoryService(
    private val sharedPrefs: SharedPreferences,
    private val cb: () -> Unit
) {

    init {
        val json = sharedPrefs.getString(SEARCH_HISTORY, null)
        val type = object : TypeToken<MutableList<iTunesAPITrack>>() {}.type
        val list = Gson().fromJson<MutableList<iTunesAPITrack>>(json, type)

        if (list != null && list.isNotEmpty()) {
            searchHistoryList.addAll(list)
        }
    }

    fun add(track: iTunesAPITrack) {
        var index = -1
        var isContains = false

        for ((i, t) in searchHistoryList.withIndex()) {
            isContains = t.trackId == track.trackId
            index = if (isContains) i else -1

            if (isContains) break
        }

        if (isContains) {
            searchHistoryList.removeAt(index)
        }

        if (searchHistoryList.size == 10) {
            searchHistoryList.removeAt(searchHistoryList.lastIndex)
        }

        searchHistoryList.add(0, track)

        val json = Gson().toJson(searchHistoryList)

        sharedPrefs.edit().putString(SEARCH_HISTORY, json).apply()

        cb()
    }

    fun get(): List<iTunesAPITrack> {
        return searchHistoryList
    }

    fun clean() {
        searchHistoryList.clear()
        sharedPrefs.edit().remove(SEARCH_HISTORY).apply()
        cb()
    }
}