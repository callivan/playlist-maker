package com.example.playlistmaker.search.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.player.ui.activities.TrackActivity
import com.example.playlistmaker.search.ui.adapters.SearchHistoryAdapter
import com.example.playlistmaker.search.ui.adapters.TracksAdapter
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewModels.SearchViewModel
import com.google.gson.Gson

class SearchActivity : ComponentActivity() {

    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModel.getViewModelFactory(this)
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
            binding.btnClean.visibility = btnCleanVisibility(text)

            if (binding.inputSearch.text.toString() != text) {
                binding.inputSearch.setText(text)
            }

            if (text.isEmpty()) {
                viewModel.resetSearchScreenStateLiveData()
            }
        }

        viewModel.getSearchScreenStateLiveData().observe(this) { state ->
            runOnUiThread {
                when (state) {
                    is SearchScreenState.Init -> {
                        println("INIT")
                        binding.emptyLayout.visibility = View.GONE
                        binding.errorLayout.visibility = View.GONE
                        binding.recyclerViewTracks.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.searchHistory.visibility = View.GONE
                    }

                    is SearchScreenState.Pending -> {
                        println("PENDING")
                        binding.emptyLayout.visibility = View.GONE
                        binding.errorLayout.visibility = View.GONE
                        binding.recyclerViewTracks.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.searchHistory.visibility = View.GONE
                    }

                    is SearchScreenState.Content -> {
                        println("CONTENT")
                        binding.emptyLayout.visibility = View.GONE
                        binding.errorLayout.visibility = View.GONE
                        binding.recyclerViewTracks.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.searchHistory.visibility = View.GONE

                        binding.recyclerViewTracks.adapter =
                            TracksAdapter(state.tracks, onClick = ::onClickTrack)
                    }

                    is SearchScreenState.Empty -> {
                        println("EMPTY")
                        binding.emptyLayout.visibility = View.VISIBLE
                        binding.errorLayout.visibility = View.GONE
                        binding.recyclerViewTracks.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.searchHistory.visibility = View.GONE
                    }

                    is SearchScreenState.Error -> {
                        println("ERROR")
                        binding.emptyLayout.visibility = View.GONE
                        binding.errorLayout.visibility = View.VISIBLE
                        binding.recyclerViewTracks.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.searchHistory.visibility = View.GONE
                    }
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            viewModel.getTracks()
        }

        binding.back.setNavigationOnClickListener {
            finish()
        }

        binding.btnClean.setOnClickListener {
            viewModel.cleanInputTextLiveData()

            val view: View? = this.currentFocus

            if (view != null) {
                hideKeyboard(view)
            }

            viewModel.resetSearchScreenStateLiveData()
            viewModel.resetInputTextLiveData()
        }

        binding.btnCleanHistory.setOnClickListener {
            viewModel.cleanSearchHistory()

            onChangedHistory()

            viewModel.resetSearchScreenStateLiveData()
        }

        binding.inputSearch.addTextChangedListener(viewModel.textWatcher())

        binding.inputSearch.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryVisibility(hasFocus)
        }

        binding.inputSearch.setOnClickListener {
            searchHistoryVisibility(true)
        }
    }

    private fun btnCleanVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchHistoryVisibility(hasFocus: Boolean) {
        binding.searchHistory.visibility = if (hasFocus && viewModel.getSearchHistory()
                .isNotEmpty() && binding.inputSearch.text.isEmpty()
        ) View.VISIBLE else View.GONE

        onInitHistory()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onChangedHistory() {
        binding.recyclerViewHistorySearch.adapter?.notifyDataSetChanged()
    }

    private fun onInitHistory() {
        binding.recyclerViewHistorySearch.adapter =
            SearchHistoryAdapter(viewModel.getSearchHistory(), onClick = ::onClickTrack)
    }

    private fun onClickTrack(track: TrackUI, remember: Boolean) {
        val intent = Intent(this, TrackActivity::class.java)

        intent.putExtra(Const.TRACK, Gson().toJson(track))

        startActivity(intent)

        if (remember) {
            viewModel.addInSearchHistory(track)
            onChangedHistory()
        }
    }
}