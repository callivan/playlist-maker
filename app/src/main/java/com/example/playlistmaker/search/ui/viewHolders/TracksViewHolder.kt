package com.example.playlistmaker.search.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.utils.Utils

class TracksViewHolder(private val binding: TrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val glide = Glide.with(itemView)

    fun bind(model: TrackUI) {
        binding.trackName.text = model.trackName
        binding.trackArtist.text = model.artistName
        binding.trackTime.text = Utils.msToMinSec(model.trackTimeMillis)

        glide.load(model.artworkUrl100).centerCrop().placeholder(R.drawable.placeholder)
            .into(binding.trackPreview)
    }

}