package com.example.playlistmaker.presentation.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.models.TrackUI
import com.example.playlistmaker.utils.Utils

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val trackPreview: ImageView = itemView.findViewById(R.id.trackPreview)

    private val glide = Glide.with(itemView)

    fun bind(model: TrackUI) {
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = Utils.msToMinSec(model.trackTimeMillis)
        glide.load(model.artworkUrl100).centerCrop().placeholder(R.drawable.placeholder)
            .into(trackPreview)
    }

}