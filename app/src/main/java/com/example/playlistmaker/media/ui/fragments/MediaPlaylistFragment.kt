package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaPlaylistBinding
import com.example.playlistmaker.media.ui.adapters.PlaylistsAdapter
import com.example.playlistmaker.media.ui.models.PlaylistsScreenState
import com.example.playlistmaker.media.ui.viewModels.PlaylistsViewModel
import com.example.playlistmaker.utils.CustomGridLayoutItemDecoration
import com.example.playlistmaker.utils.Utils
import com.example.playlistmaker.utils.Utils.Companion.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MediaPlaylistFragment : Fragment() {
    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var binding: FragmentMediaPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylists()

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.addItemDecoration(
            CustomGridLayoutItemDecoration.createGridSpacingItemDecoration(
                spanCount = 2,
                columnSpacing = 8f.dpToPx(requireContext()).toInt(),
                rowSpacing = 16f.dpToPx(requireContext()).toInt()
            )
        )

        viewModel.getPlaylistsScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            requireActivity().runOnUiThread {
                when (state) {
                    is PlaylistsScreenState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.emptyLayout.isVisible = false
                        binding.recyclerView.isVisible = false
                    }

                    is PlaylistsScreenState.Empty -> {
                        binding.progressBar.isVisible = false
                        binding.emptyLayout.isVisible = true
                        binding.recyclerView.isVisible = false
                    }

                    is PlaylistsScreenState.Content -> {
                        binding.progressBar.isVisible = false
                        binding.emptyLayout.isVisible = false
                        binding.recyclerView.isVisible = true
                        binding.recyclerView.adapter = PlaylistsAdapter(state.playlists)
                    }
                }
            }
        }

        binding.btnCreatePlaylist.setOnClickListener {
            Utils.debounce<Unit>(300L, viewLifecycleOwner.lifecycleScope, false) {
                findNavController().navigate(R.id.action_mediaFragment_to_mediaPlaylistCreatorFragment)
            }(Unit)
        }
    }
}