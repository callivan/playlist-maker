package com.example.playlistmaker.player.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistHorizontalViewBinding
import com.example.playlistmaker.player.ui.models.PlaylistUI

class PlaylistsViewHolder(private val binding: PlaylistHorizontalViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val glide = Glide.with(itemView)

    fun bind(model: PlaylistUI) {
        val tracksCount = model.tracksId.size

        binding.playlistName.text = model.name
        binding.playlistTracksCount.text = binding.root.context.resources.getQuantityString(
            R.plurals.tracks_count, tracksCount, tracksCount
        )

        glide.load(model.img).centerCrop().placeholder(R.drawable.placeholder)
            .into(binding.playlistImg)
    }

}