package com.example.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewHolders.TracksViewHolder

class SearchHistoryAdapter(
    private val tracks: List<TrackUI>,
    private val onClick: (track: TrackUI) -> Unit
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return TracksViewHolder(TrackViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val item = tracks[position]

        holder.itemView.setOnClickListener {
            onClick(item)
        }

        holder.bind(item)
    }

    override fun getItemCount(): Int = tracks.size
}