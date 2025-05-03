package com.example.playlistmaker.playlist.ui.fragments

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.playlist.ui.adapters.TracksAdapter
import com.example.playlistmaker.playlist.ui.models.PlaylistScreenState
import com.example.playlistmaker.playlist.ui.models.PlaylistUI
import com.example.playlistmaker.playlist.ui.models.TrackUI
import com.example.playlistmaker.playlist.ui.viewModels.PlaylistViewModel
import com.example.playlistmaker.utils.Utils
import com.example.playlistmaker.utils.Utils.Companion.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit
import kotlin.getValue

class PlaylistFragment : Fragment() {
    private val viewModel by viewModel<PlaylistViewModel>()

    private lateinit var binding: FragmentPlaylistBinding

    private var onClick: ((track: TrackUI) -> Unit)? = null

    private var playlist: PlaylistUI = PlaylistUI()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = Gson().fromJson(arguments?.getString(Const.PLAYLIST), PlaylistUI::class.java)

        viewModel.getPlaylistTracks(playlist.id)

        onClick = Utils.debounce<TrackUI>(
            300L, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            onClickTrack(track)
        }

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

        binding.playlistBottomSheet.background = cornerShape
        binding.playlistBottomSheetMenu.background = cornerShape

        BottomSheetBehavior.from(binding.playlistBottomSheet).apply {

            view.post {
                val bottomSheetContainerHeight = binding.bottomSheetContainer.height

                state = BottomSheetBehavior.STATE_COLLAPSED
                peekHeight = bottomSheetContainerHeight
            }
        }

        val bottomSheetMenu = BottomSheetBehavior.from(binding.playlistBottomSheetMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.getPlaylistScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            requireActivity().runOnUiThread {
                when (state) {
                    is PlaylistScreenState.Empty -> {
                        binding.playlistBottomSheet.isVisible = false
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = false
                    }

                    is PlaylistScreenState.Loading -> {
                        binding.playlistBottomSheet.isVisible = false
                        binding.progressBar.isVisible = true
                        binding.recyclerView.isVisible = false
                    }

                    is PlaylistScreenState.Content -> {
                        val playlist = state.state.playlist
                        val tracks = state.state.tracks
                        val playlistMinutesCount =
                            TimeUnit.MILLISECONDS.toMinutes(tracks.sumOf { it.trackTimeMillis })

                        binding.playlistBottomSheet.isVisible = state.state.tracks.isNotEmpty()
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = true
                        binding.recyclerView.adapter = TracksAdapter(
                            tracks = tracks,
                            onClick = { track -> onClick?.let { it(track) } },
                            onLongClick = ::onLongClickTrack
                        )

                        Glide.with(requireContext()).load(playlist.img).centerCrop()
                            .placeholder(R.drawable.placeholder).into(binding.image)

                        Glide.with(requireContext()).load(playlist.img).centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(binding.playlistHorizontal.playlistImg)

                        binding.playlistHorizontal.playlistName.text = playlist.name
                        binding.playlistHorizontal.playlistTracksCount.text =
                            requireContext().resources.getQuantityString(
                                R.plurals.tracks_count, tracks.size, tracks.size
                            )

                        binding.name.text = playlist.name
                        binding.description.text = playlist.description
                        binding.minutes.text = requireContext().resources.getQuantityString(
                            R.plurals.minutes,
                            if (playlistMinutesCount > Int.MAX_VALUE) Int.MAX_VALUE else playlistMinutesCount.toInt(),
                            playlistMinutesCount
                        )

                        binding.tracksCount.text = requireContext().resources.getQuantityString(
                            R.plurals.tracks_count, tracks.size, tracks.size
                        )


                        binding.btnShare.setOnClickListener {
                            onSharePlaylist(tracks)
                        }

                        binding.btnMenuShare.setOnClickListener {
                            onSharePlaylist(tracks)
                        }

                        binding.btnMenuUpdate.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_playlistFragment_to_mediaPlaylistCreatorFragment, bundleOf(
                                    Const.PLAYLIST to Gson().toJson(playlist)
                                )
                            )
                        }

                        binding.btnMenuDelete.setOnClickListener {
                            AlertDialog.Builder(requireContext())
                                .setTitle(getString(R.string.playlist_delete))
                                .setMessage(getString(R.string.want_delete_playlist))
                                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                                    viewModel.deletePlaylist(playlist.id)

                                    findNavController().navigateUp()

                                    dialog.dismiss()
                                }.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                                    dialog.dismiss()
                                }.create().show()
                        }
                    }
                }
            }
        }

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnMenu.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun onClickTrack(track: TrackUI) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_playerFragment, bundleOf(
                Const.TRACK to Gson().toJson(track)
            )
        )
    }

    private fun onSharePlaylist(tracks: List<TrackUI>) {
        if (tracks.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.playlist_empty))
                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        } else {

            val tracksList = tracks.mapIndexed { index, track ->
                "${index + 1}. ${track.artistName} - ${track.trackName} ${
                    Utils.msToMinSec(
                        track.trackTimeMillis
                    )
                }"
            }.joinToString("\n")

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plane"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "${playlist.name}\n${playlist.description}\n${
                    requireContext().resources.getQuantityString(
                        R.plurals.tracks_count, tracks.size, tracks.size
                    )
                }\n${tracksList}"
            )

            startActivity(
                Intent.createChooser(
                    intent,
                    "Share APK"
                )
            )
        }
    }

    private fun onLongClickTrack(track: TrackUI) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.modal_delete_track_title))
            .setMessage(getString(R.string.modal_want_delete_track_text))
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                viewModel.deletePlaylistTrack(playlist.id, track.trackId)
                viewModel.getPlaylistTracks(playlist.id)
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }
}