package com.example.playlistmaker.player.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.player.domain.models.PlayerScreenState
import com.example.playlistmaker.player.ui.viewModels.TrackViewModel
import com.example.playlistmaker.utils.Utils

class TrackActivity : ComponentActivity() {
    private val viewModel by viewModels<TrackViewModel> {
        TrackViewModel.getViewModelFactory(this)
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

        binding.btnPlay.isEnabled = false

        viewModel.getPlayerScreenStateLiveData().observe(this) { state ->
            when (state) {
                is PlayerScreenState.Prepeared -> {
                    onPlayerPrepared()
                }

                is PlayerScreenState.Completed -> {
                    onPlayerComplete()
                }
            }
        }

        viewModel.getTrackLiveData().observe(this) { track ->
            Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .centerCrop().placeholder(R.drawable.placeholder).into(binding.trackImage)


            binding.trackName.text = track.trackName
            binding.trackAuthor.text = track.artistName
            binding.trackDuration.text = utils.msToMinSec(track.trackTimeMillis)

            if (track.collectionName != null) {
                binding.trackAlbum.text = track.collectionName
            } else {
                group.visibility = View.GONE
            }

            binding.trackYear.text = utils.dateToYear(track.releaseDate)
            binding.trackGenre.text = track.primaryGenreName
            binding.trackCountry.text = track.country
        }

        viewModel.getPlayerProgressLiveData().observe(this) { playerState ->
            binding.trackTime.text = utils.msToMinSec(playerState.progress.toLong())
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnPlay.setOnClickListener {
            viewModel.playerController(::onPlayerStart, ::onPlayerPause)
        }
    }

    private fun onPlayerPrepared() {
        binding.btnPlay.isEnabled = true
    }

    private fun onPlayerComplete() {
        binding.btnPlay.setImageResource(R.drawable.play)
        binding.trackTime.text = "00:00"
    }

    private fun onPlayerStart() {
        binding.btnPlay.setImageResource(R.drawable.pause)
    }

    private fun onPlayerPause() {
        binding.btnPlay.setImageResource(R.drawable.play)
    }

    override fun onPause() {
        super.onPause()

        onPlayerPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.release()
    }
}