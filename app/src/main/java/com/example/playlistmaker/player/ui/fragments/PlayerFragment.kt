package com.example.playlistmaker.player.ui.fragments

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.ui.adapters.PlaylistsAdapter
import com.example.playlistmaker.player.ui.models.BottomSheetScreenState
import com.example.playlistmaker.player.ui.models.TrackUI
import com.example.playlistmaker.player.ui.models.PlayerScreenState
import com.example.playlistmaker.player.ui.models.PlaylistUI
import com.example.playlistmaker.player.ui.viewModels.PlayerViewModel
import com.example.playlistmaker.utils.CustomSnackbar
import com.example.playlistmaker.utils.Utils
import com.example.playlistmaker.utils.Utils.Companion.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.Long
import kotlin.String

class PlayerFragment : Fragment() {
    private val viewModel by viewModel<PlayerViewModel>()

    private lateinit var binding: FragmentPlayerBinding

    private var onClickPlaylistDebounce: ((playlist: PlaylistUI) -> Unit)? = null

    private var track: TrackUI = TrackUI()

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

        onClickPlaylistDebounce = Utils.debounce<PlaylistUI>(
            300L,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            onClickPlaylist(playlist = playlist)
        }

        val bottomSheet = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            binding.shadow.isVisible = false
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        val btnClickAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.button_click)
        val bottomSheetHideShadowAnimation =
            AnimationUtils.loadAnimation(requireActivity(), R.anim.hide)
        val bottomSheetShowShadowAnimation =
            AnimationUtils.loadAnimation(requireActivity(), R.anim.show)

        val cornerShape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(
                16f.dpToPx(requireContext()),
                16f.dpToPx(requireContext()),
                16f.dpToPx(requireContext()),
                16f.dpToPx(requireContext()),
                0f,
                0f,
                0f,
                0f
            )
        }

        track = Gson().fromJson(arguments?.getString(Const.TRACK), TrackUI::class.java)

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

        binding.playlistBottomSheet.background = cornerShape

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

        viewModel.getBottomSheetScreenScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            requireActivity().runOnUiThread {
                when (state) {
                    is BottomSheetScreenState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.recyclerView.isVisible = false
                    }

                    is BottomSheetScreenState.Empty -> {
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = false
                    }

                    is BottomSheetScreenState.Update -> {
                        CustomSnackbar.createSnackbar(
                            requireView(),
                            getString(
                                if (state.state) R.string.track_add_in_playlist else R.string.track_exists_in_playlist,
                                state.playlist.name
                            )
                        ).show()

                        if (state.state) {
                            binding.shadow.isVisible = false
                            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                            binding.shadow.startAnimation(bottomSheetHideShadowAnimation)
                        }
                    }

                    is BottomSheetScreenState.Content -> {
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = true
                        binding.recyclerView.adapter = PlaylistsAdapter(
                            playlists = state.playlists,
                            onClick = { playlist ->
                                onClickPlaylistDebounce?.let {
                                    it(playlist)
                                }
                            }
                        )
                    }
                }
            }
        }

        bottomSheet.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    binding.shadow.isVisible = false
                    binding.shadow.startAnimation(bottomSheetHideShadowAnimation)
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    viewModel.getPlaylists()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

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

        binding.createPlaylist.setOnClickListener {
            binding.shadow.isVisible = true
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.shadow.startAnimation(bottomSheetShowShadowAnimation)
        }

        binding.btnCreatePlaylist.setOnClickListener {
            Utils.debounce<Unit>(300L, viewLifecycleOwner.lifecycleScope, false) {
                findNavController().navigate(
                    R.id.action_playerFragment_to_mediaPlaylistCreatorFragment, bundleOf(
                        Const.TRACK to Gson().toJson(track)
                    )
                )

                viewModel.cleanBottomSheetScreenStateLiveData()

            }(Unit)
        }
    }

    private fun onClickPlaylist(playlist: PlaylistUI) {
        viewModel.insertPlaylistTrack(playlist = playlist, track = track)
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