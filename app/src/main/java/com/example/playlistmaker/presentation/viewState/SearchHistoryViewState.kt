package com.example.playlistmaker.presentation.viewState

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.ISharedPreferences
import com.example.playlistmaker.data.sharedPreferences.TracksHistorySharedPreferences
import com.example.playlistmaker.presentation.adapters.SearchHistoryAdapter
import com.example.playlistmaker.presentation.models.TrackUI

const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"

class SearchHistoryViewState(private val recyclerView: RecyclerView, private val context: Context) :
    ISharedPreferences<List<TrackUI>, TrackUI> {
    private val sharedPrefs = context.getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)

    private val searchHistory = TracksHistorySharedPreferences(
        sharedPrefs,
        { recyclerView.adapter?.notifyDataSetChanged() })


    fun init(onClick: (track: TrackUI, remember: Boolean) -> Unit) {
        recyclerView.adapter = SearchHistoryAdapter(searchHistory.get(), onClick)
    }


    override fun get(): List<TrackUI> {
        return searchHistory.get()
    }

    override fun add(track: TrackUI) {
        searchHistory.add(track)
    }

    override fun clean() {
        searchHistory.clean()
    }
}