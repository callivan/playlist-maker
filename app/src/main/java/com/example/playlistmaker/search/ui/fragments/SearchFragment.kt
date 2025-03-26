package com.example.playlistmaker.search.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.ui.adapters.SearchHistoryAdapter
import com.example.playlistmaker.search.ui.adapters.TracksAdapter
import com.example.playlistmaker.search.ui.models.TracksSearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewModels.SearchViewModel
import com.example.playlistmaker.utils.Utils
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var binding: FragmentSearchBinding

    private var onClickTrack: ((track: TrackUI) -> Unit)? = null
    private var onClickHistoryTrack: ((track: TrackUI) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickTrack = Utils.debounce<TrackUI>(
            300L,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            onClick(track)

            viewModel.addTrackInHistory(track)
            onChangedHistory()
        }

        onClickHistoryTrack = Utils.debounce<TrackUI>(
            300L,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track -> onClick(track) }

        viewModel.getInputTextLiveData().observe(viewLifecycleOwner) { text ->
            binding.btnClean.isVisible = text.isNotEmpty()

            if (binding.inputSearch.text.toString() != text) {
                binding.inputSearch.setText(text)
            }
        }

        viewModel.getTracksSearchScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            requireActivity().runOnUiThread {
                when (state) {
                    is TracksSearchScreenState.Init -> {
                        binding.emptyLayout.isVisible = false
                        binding.errorLayout.isVisible = false
                        binding.recyclerViewTracks.isVisible = false
                        binding.progressBar.isVisible = false
                        binding.searchHistory.isVisible = false
                    }

                    is TracksSearchScreenState.Pending -> {
                        binding.emptyLayout.isVisible = false
                        binding.errorLayout.isVisible = false
                        binding.recyclerViewTracks.isVisible = false
                        binding.progressBar.isVisible = true
                        binding.searchHistory.isVisible = false
                    }

                    is TracksSearchScreenState.Content -> {
                        binding.emptyLayout.isVisible = false
                        binding.errorLayout.isVisible = false
                        binding.recyclerViewTracks.isVisible = true
                        binding.progressBar.isVisible = false
                        binding.searchHistory.isVisible = false

                        if (onClickTrack != null) {
                            binding.recyclerViewTracks.adapter =
                                TracksAdapter(state.tracks, onClick = onClickTrack!!)
                        }
                    }

                    is TracksSearchScreenState.Empty -> {
                        binding.emptyLayout.isVisible = true
                        binding.errorLayout.isVisible = false
                        binding.recyclerViewTracks.isVisible = false
                        binding.progressBar.isVisible = false
                        binding.searchHistory.isVisible = false
                    }

                    is TracksSearchScreenState.Error -> {
                        binding.emptyLayout.isVisible = false
                        binding.errorLayout.isVisible = true
                        binding.recyclerViewTracks.isVisible = false
                        binding.progressBar.isVisible = false
                        binding.searchHistory.isVisible = false
                    }

                    is TracksSearchScreenState.History -> {
                        if (onClickHistoryTrack != null) {
                            binding.recyclerViewHistorySearch.adapter =
                                SearchHistoryAdapter(
                                    state.tracks,
                                    onClick = onClickHistoryTrack!!
                                )
                        }
                        historyVisibility(binding.inputSearch.hasFocus(), state.tracks.isNotEmpty())

                        onChangedHistory()
                    }
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            viewModel.getTracks()
        }

        binding.btnClean.setOnClickListener {
            val view: View? = requireActivity().currentFocus

            if (view != null) {
                hideKeyboard(view)
            }

            viewModel.cleanInputTextLiveData()
        }

        binding.btnCleanHistory.setOnClickListener {
            viewModel.cleanTracksHistory()

            onChangedHistory()
        }

        binding.inputSearch.addTextChangedListener(viewModel.textWatcher())

        binding.inputSearch.setOnFocusChangeListener { _, hasFocus ->
            viewModel.getTracksHistory()
        }

        binding.inputSearch.setOnClickListener {
            viewModel.getTracksHistory()
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun historyVisibility(hasFocus: Boolean, historyIsNotEmpty: Boolean) {
        binding.searchHistory.isVisible =
            binding.inputSearch.text.isEmpty() && hasFocus && historyIsNotEmpty
    }

    private fun onChangedHistory() {
        binding.recyclerViewHistorySearch.adapter?.notifyDataSetChanged()
    }

    private fun onClick(track: TrackUI) {
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            bundleOf(
                Const.TRACK to Gson().toJson(track)
            )
        )

        viewModel.cleanTracksSearchScreenState()
    }
}