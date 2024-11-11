package com.example.playlistmaker.trackReciclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.iTunesAPIService.iTunesAPITrack
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val trackPreview: ImageView = itemView.findViewById(R.id.trackPreview)

    private val glide = Glide.with(itemView)
    private val dateFormatter by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun bind(model: iTunesAPITrack) {
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = dateFormatter.format(model.trackTimeMillis)
        glide.load(model.artworkUrl100).centerCrop().placeholder(R.drawable.placeholder)
            .into(trackPreview)
    }

}