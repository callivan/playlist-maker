package com.example.playlistmaker.searchHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.iTunesAPIService.iTunesAPITrack
import com.example.playlistmaker.trackReciclerView.TrackViewHolder

class SearchHistoryAdapter(
    private val tracks: List<iTunesAPITrack>,
    private val cb: (track: iTunesAPITrack, remember: Boolean) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)

        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            cb(tracks[position], false)
        }

        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}