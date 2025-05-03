package com.example.playlistmaker.media.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistVerticalViewBinding
import com.example.playlistmaker.media.ui.models.PlaylistUI
import com.example.playlistmaker.media.ui.viewHolders.PlaylistsViewHolder

class PlaylistsAdapter(
    private val playlists: List<PlaylistUI>,
) : RecyclerView.Adapter<PlaylistsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return PlaylistsViewHolder(
            PlaylistVerticalViewBinding.inflate(layoutInspector, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size
}