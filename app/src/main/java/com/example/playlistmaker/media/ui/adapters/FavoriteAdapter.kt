package com.example.playlistmaker.media.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.media.ui.models.TrackUI
import com.example.playlistmaker.media.ui.viewHolders.FavoriteViewHolder

class FavoriteAdapter(
    private val tracks: List<TrackUI>,
    private val onClick: (track: TrackUI) -> Unit
) : RecyclerView.Adapter<FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return FavoriteViewHolder(TrackViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            onClick(tracks[position])
        }

        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}