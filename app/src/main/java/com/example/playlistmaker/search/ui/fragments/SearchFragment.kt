package com.example.playlistmaker.search.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.media.ui.models.BindingFragment
import com.example.playlistmaker.player.ui.fragments.PlayerFragment
import com.example.playlistmaker.search.ui.adapters.SearchHistoryAdapter
import com.example.playlistmaker.search.ui.adapters.TracksAdapter
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.models.TracksSearchScreenState
import com.example.playlistmaker.search.ui.viewModels.SearchViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    private val viewModel by viewModel<SearchViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                        binding.recyclerViewTracks.adapter =
                            TracksAdapter(state.tracks, onClick = ::onClickTrack)
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
                        binding.recyclerViewHistorySearch.adapter =
                            SearchHistoryAdapter(
                                state.tracks,
                                onClick = ::onClickTrack
                            )

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

        binding.back.setNavigationOnClickListener {
            val fragment = parentFragmentManager.findFragmentById(R.id.root_fragment)

            if (fragment != null) {
                parentFragmentManager.beginTransaction()
                    .remove(fragment).commit()
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun historyVisibility(hasFocus: Boolean, historyIsNotEmpty: Boolean) {
        binding.searchHistory.isVisible =
            binding.inputSearch.text.isEmpty() && hasFocus && historyIsNotEmpty
    }

    private fun onChangedHistory() {
        binding.recyclerViewHistorySearch.adapter?.notifyDataSetChanged()
    }

    private fun onClickTrack(track: TrackUI, remember: Boolean) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.root_fragment, PlayerFragment.newInstance(track = Gson().toJson(track))).commit()


        if (remember) {
            viewModel.addTrackInHistory(track)
            onChangedHistory()
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}