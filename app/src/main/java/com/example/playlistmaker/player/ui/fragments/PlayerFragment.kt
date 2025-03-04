package com.example.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.ui.models.BindingFragment
import com.example.playlistmaker.player.ui.models.PlayerScreenState
import com.example.playlistmaker.player.ui.models.TrackUI
import com.example.playlistmaker.player.ui.viewModels.TrackViewModel
import com.example.playlistmaker.search.ui.fragments.SearchFragment
import com.example.playlistmaker.utils.Utils
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {
    private val viewModel by viewModel<TrackViewModel>()

    private val utils = Utils

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = Gson().fromJson(
            requireArguments().getString(Const.TRACK), TrackUI::class.java
        )

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .centerCrop().placeholder(R.drawable.placeholder).into(binding.trackImage)

        binding.trackName.text = track.trackName
        binding.trackAuthor.text = track.artistName
        binding.trackDuration.text = utils.msToMinSec(track.trackTimeMillis)

        binding.trackAlbum.text = track.collectionName
        binding.group.isVisible = track.collectionName != null

        binding.trackYear.text = utils.dateToYear(track.releaseDate)
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country

        viewModel.prepare(track.previewUrl)

        viewModel.getPlayerScreenStateLiveData().observe(viewLifecycleOwner) { state ->
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
            parentFragmentManager.beginTransaction()
                .replace(R.id.root_fragment, SearchFragment.newInstance()).commit()
        }

        binding.btnPlay.setOnClickListener {
            viewModel.playbackController()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.release()
    }

    companion object {
        fun newInstance(track: String) = PlayerFragment().apply {
            arguments = bundleOf(Const.TRACK to track)
        }
    }
}