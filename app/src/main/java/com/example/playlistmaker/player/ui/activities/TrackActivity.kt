package com.example.playlistmaker.player.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.player.ui.models.TrackUI
import com.example.playlistmaker.player.ui.models.PlayerScreenState
import com.example.playlistmaker.player.ui.viewModels.TrackViewModel
import com.example.playlistmaker.utils.Utils
import com.google.gson.Gson

class TrackActivity : AppCompatActivity() {
    private val viewModel by viewModels<TrackViewModel> {
        TrackViewModel.getViewModelFactory()
    }

    private lateinit var binding: ActivityTrackBinding

    private lateinit var group: Group

    private val utils = Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityTrackBinding.inflate(layoutInflater)

        setContentView(binding.root)

        group = findViewById(R.id.group)

        val track = Gson().fromJson(intent.getStringExtra(Const.TRACK), TrackUI::class.java)

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .centerCrop().placeholder(R.drawable.placeholder).into(binding.trackImage)

        binding.trackName.text = track.trackName
        binding.trackAuthor.text = track.artistName
        binding.trackDuration.text = utils.msToMinSec(track.trackTimeMillis)

        binding.trackAlbum.text = track.collectionName
        group.isVisible = track.collectionName != null

        binding.trackYear.text = utils.dateToYear(track.releaseDate)
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country

        viewModel.prepare(track.previewUrl)

        viewModel.getPlayerScreenStateLiveData().observe(this) { state ->
            when (state) {
                is PlayerScreenState.Pending -> {
                    binding.btnPlay.isEnabled = false
                }

                is PlayerScreenState.Prepared -> {
                    binding.btnPlay.isEnabled = true
                }

                is PlayerScreenState.Playing -> {
                    binding.btnPlay.setImageResource(R.drawable.pause)
                    binding.trackTime.text = utils.msToMinSec(state.progress.toLong())
                }

                is PlayerScreenState.Paused -> {
                    binding.btnPlay.setImageResource(R.drawable.play)
                }

                is PlayerScreenState.Released, PlayerScreenState.Completed -> {
                    binding.btnPlay.setImageResource(R.drawable.play)
                    binding.trackTime.text = "00:00"
                }
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnPlay.setOnClickListener {
            viewModel.playbackController()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}