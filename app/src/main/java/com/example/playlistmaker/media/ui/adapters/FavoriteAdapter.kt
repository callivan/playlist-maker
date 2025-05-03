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
        val item = tracks[position]

        holder.itemView.setOnClickListener {
            onClick(item)
        }

        holder.bind(item)
    }

    override fun getItemCount(): Int = tracks.size
}