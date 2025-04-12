package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.databinding.FragmentMediaFavoriteBinding
import com.example.playlistmaker.media.ui.adapters.FavoriteAdapter
import com.example.playlistmaker.media.ui.models.FavoriteScreenState
import com.example.playlistmaker.media.ui.models.TrackUI
import com.example.playlistmaker.media.ui.viewModels.FavoriteViewModel
import com.example.playlistmaker.utils.Utils
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MediaFavoriteFragment : Fragment() {
    private val viewModel by viewModel<FavoriteViewModel>()

    private lateinit var binding: FragmentMediaFavoriteBinding

    private var onClickTrack: ((track: TrackUI) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteTracks()

        onClickTrack = Utils.debounce<TrackUI>(
            300L,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            onClick(track)
        }

        viewModel.getFavoriteScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            requireActivity().runOnUiThread {
                when (state) {
                    is FavoriteScreenState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.emptyLayout.isVisible = false
                        binding.recyclerView.isVisible = false
                    }

                    is FavoriteScreenState.Empty -> {
                        binding.progressBar.isVisible = false
                        binding.emptyLayout.isVisible = true
                        binding.recyclerView.isVisible = false
                    }

                    is FavoriteScreenState.Content -> {
                        binding.progressBar.isVisible = false
                        binding.emptyLayout.isVisible = false
                        binding.recyclerView.isVisible = true

                        if (onClickTrack != null) {
                            binding.recyclerView.adapter =
                                FavoriteAdapter(state.tracks, onClick = onClickTrack!!)
                        }
                    }
                }
            }
        }
    }

    private fun onClick(track: TrackUI) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerFragment,
            bundleOf(
                Const.TRACK to Gson().toJson(track)
            )
        )
    }
}