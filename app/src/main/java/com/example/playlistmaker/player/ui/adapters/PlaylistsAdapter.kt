package com.example.playlistmaker.player.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistHorizontalViewBinding
import com.example.playlistmaker.player.ui.models.PlaylistUI
import com.example.playlistmaker.player.ui.viewHolders.PlaylistsViewHolder

class PlaylistsAdapter(
    private val playlists: List<PlaylistUI>,
    private val onClick: (playlist: PlaylistUI) -> Unit
) : RecyclerView.Adapter<PlaylistsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return PlaylistsViewHolder(
            PlaylistHorizontalViewBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val item = playlists[position]

        holder.itemView.setOnClickListener {
            onClick(item)
        }

        holder.bind(item)
    }

    override fun getItemCount(): Int = playlists.size
}