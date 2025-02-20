package com.example.playlistmaker.search.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.player.ui.activities.TrackActivity
import com.example.playlistmaker.search.ui.adapters.SearchHistoryAdapter
import com.example.playlistmaker.search.ui.adapters.TracksAdapter
import com.example.playlistmaker.search.ui.models.TracksSearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewModels.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModel.getViewModelFactory()
    }

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.getInputTextLiveData().observe(this) { text ->
            binding.btnClean.isVisible = text.isNotEmpty()

            if (binding.inputSearch.text.toString() != text) {
                binding.inputSearch.setText(text)
            }
        }

        viewModel.getTracksSearchScreenStateLiveData().observe(this) { state ->
            runOnUiThread {
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
            val view: View? = this.currentFocus

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
            finish()
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

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
        val intent = Intent(this, TrackActivity::class.java)

        intent.putExtra(Const.TRACK, Gson().toJson(track))

        startActivity(intent)

        if (remember) {
            viewModel.addTrackInHistory(track)
            onChangedHistory()
        }
    }
}