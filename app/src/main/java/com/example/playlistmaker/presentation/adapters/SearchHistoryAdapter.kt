package com.example.playlistmaker.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.models.TrackUI
import com.example.playlistmaker.presentation.viewHolders.TracksViewHolder

class SearchHistoryAdapter(
    private val tracks: List<TrackUI>,
    private val cb: (track: TrackUI, remember: Boolean) -> Unit
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)

        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            cb(tracks[position], false)
        }

        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}