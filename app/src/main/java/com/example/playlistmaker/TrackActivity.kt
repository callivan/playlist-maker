package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.playlistmaker.iTunesAPIService.iTunesAPITrack
import com.example.playlistmaker.utils.Date
import com.google.gson.Gson

class TrackActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton

    private lateinit var trackImg: ImageView

    private lateinit var trackName: TextView
    private lateinit var trackAuthor: TextView
    private lateinit var trackDuration: TextView
    private lateinit var trackAlbum: TextView
    private lateinit var trackYear: TextView
    private lateinit var trackGenre: TextView
    private lateinit var trackCountry: TextView

    private val date = Date()

    private lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val track = Gson().fromJson(intent.getStringExtra(TRACK), iTunesAPITrack::class.java)

        btnBack = findViewById(R.id.btnBack)

        trackImg = findViewById(R.id.trackImage)

        trackName = findViewById(R.id.trackName)
        trackAuthor = findViewById(R.id.trackAuthor)
        trackDuration = findViewById(R.id.trackDuration)
        trackAlbum = findViewById(R.id.trackAlbum)
        trackYear = findViewById(R.id.trackYear)
        trackGenre = findViewById(R.id.trackGenre)
        trackCountry = findViewById(R.id.trackCountry)

        group = findViewById(R.id.group)

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .centerCrop().placeholder(R.drawable.placeholder)
            .into(trackImg)

        trackName.text = track.trackName
        trackAuthor.text = track.artistName
        trackDuration.text = date.msToMinSec(track.trackTimeMillis)

        if (track.collectionName != null) {
            trackAlbum.text = track.collectionName
        } else {
            group.visibility = View.GONE
        }

        trackYear.text = date.dateToYear(track.releaseDate)
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        btnBack.setOnClickListener {
            finish()
        }
    }
}