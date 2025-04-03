package com.example.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.ui.models.TrackUI
import com.example.playlistmaker.player.ui.models.PlayerScreenState
import com.example.playlistmaker.player.ui.viewModels.TrackViewModel
import com.example.playlistmaker.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {
    private val viewModel by viewModel<TrackViewModel>()

    private lateinit var binding: FragmentPlayerBinding

    private val utils = Utils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnClickAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.button_click)

        val track = Gson().fromJson(arguments?.getString(Const.TRACK), TrackUI::class.java)

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

        existsInFavoriteDb(track.trackId)

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
            findNavController().navigateUp()
        }

        binding.btnPlay.setOnClickListener {
            viewModel.playbackController()
            it.startAnimation(btnClickAnimation)
        }

        binding.favor.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.existsInFavoriteDb(track.trackId).collect { state ->

                    if (state) {
                        viewModel.deleteFromFavoriteDb(track.trackId)
                    } else {
                        viewModel.insertInFavoriteDb(track)
                    }

                    printFavoriteBtn(!state)
                    it.startAnimation(btnClickAnimation)
                }
            }
        }
    }

    private fun printFavoriteBtn(state: Boolean) {
        requireActivity().runOnUiThread {
            if (state) {
                binding.favor.setImageResource(R.drawable.favorite_on)
            } else {
                binding.favor.setImageResource(R.drawable.favourite_off)
            }
        }
    }


    private fun existsInFavoriteDb(id: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.existsInFavoriteDb(id).collect { state ->
                printFavoriteBtn(state)
            }
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