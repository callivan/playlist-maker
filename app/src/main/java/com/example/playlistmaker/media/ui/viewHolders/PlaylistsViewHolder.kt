package com.example.playlistmaker.media.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistVerticalViewBinding
import com.example.playlistmaker.media.ui.models.PlaylistUI

class PlaylistsViewHolder(private val binding: PlaylistVerticalViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val glide = Glide.with(itemView)

    fun bind(model: PlaylistUI) {
        binding.playlistName.text = model.name
        binding.playlistTracksCount.text = binding.root.context.resources.getQuantityString(
            R.plurals.tracks_count, model.tracksCount, model.tracksCount
        )

        glide.load(model.img).centerCrop().placeholder(R.drawable.placeholder)
            .into(binding.playlistImg)
    }
}