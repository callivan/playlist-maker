package com.example.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewHolders.TracksViewHolder

class TracksAdapter(
    private val tracks: List<TrackUI>,
    private val onClick: (track: TrackUI, remember: Boolean) -> Unit
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return TracksViewHolder(TrackViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            onClick(tracks[position], true)
        }

        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}