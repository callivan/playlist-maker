package com.example.playlistmaker.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.components.MyTextWatcher
import com.example.playlistmaker.presentation.models.TrackUI
import com.example.playlistmaker.presentation.viewState.SearchHistoryViewState
import com.example.playlistmaker.presentation.viewState.TracksViewState
import com.example.playlistmaker.ui.models.UIState
import com.example.playlistmaker.utils.Utils
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson

const val TRACK = "track"

class SearchActivity : AppCompatActivity() {
    private var searchText = ""

    private lateinit var inputSearch: EditText

    private lateinit var progressBar: ProgressBar

    private lateinit var btnBack: MaterialToolbar
    private lateinit var btnClean: ImageButton
    private lateinit var btnUpdate: Button
    private lateinit var btnCleanSearchHistory: Button

    private lateinit var tracksRecyclerView: RecyclerView
    private lateinit var searchHistoryRecyclerView: RecyclerView

    private lateinit var emptyLayout: LinearLayout
    private lateinit var errorLayout: LinearLayout
    private lateinit var searchHistoryLayout: LinearLayout

    private lateinit var searchHistoryViewState: SearchHistoryViewState
    private lateinit var tracksViewState: TracksViewState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputSearch = findViewById(R.id.inputSearch)

        progressBar = findViewById(R.id.progressBar)

        btnBack = findViewById(R.id.back)
        btnClean = findViewById(R.id.btnClean)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnCleanSearchHistory = findViewById(R.id.btnCleanHistory)

        searchHistoryRecyclerView = findViewById(R.id.recyclerViewHistorySearch)
        tracksRecyclerView = findViewById(R.id.recyclerViewTracks)

        emptyLayout = findViewById(R.id.empty_layout)
        errorLayout = findViewById(R.id.error_layout)
        searchHistoryLayout = findViewById(R.id.searchHistory)

        searchHistoryViewState = SearchHistoryViewState(searchHistoryRecyclerView, this)
        tracksViewState =
            TracksViewState(
                tracksRecyclerView,
                ::onClickTrack,
                ::onInit,
                ::onPending,
                ::onSuccess,
                ::onError
            )

        searchHistoryViewState.init(::onClickTrack)

        tracksRecyclerView.setTag(tracksRecyclerView.id, "TRACK")

        val debounceSearchTrack =
            Utils.debounceWithThread({ tracksViewState.getTracks(searchText) }, 1000L)

        btnUpdate.setOnClickListener {
            tracksViewState.getTracks(searchText)
        }

        btnBack.setNavigationOnClickListener {
            finish()
        }

        btnClean.visibility = btnCleanVisibility(inputSearch.text)

        btnClean.setOnClickListener {
            inputSearch.setText("")

            val view: View? = this.currentFocus

            if (view != null) {
                hideKeyboard(view)
            }

            conditionUIState(UIState.INITED)
        }

        btnCleanSearchHistory.setOnClickListener {
            searchHistoryViewState.clean()
            searchHistoryLayout.visibility = View.GONE
        }

        inputSearch.addTextChangedListener(MyTextWatcher(onTextChanged = { s ->
            btnClean.visibility = btnCleanVisibility(s)

            searchText = s.toString()

            searchHistoryLayout.visibility =
                if (inputSearch.hasFocus() && searchHistoryViewState.get()
                        .isNotEmpty() && s?.isEmpty() == true
                ) View.VISIBLE else View.GONE

            debounceSearchTrack.debounce()
        }))

        inputSearch.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.visibility =
                if (hasFocus && searchHistoryViewState.get()
                        .isNotEmpty() && inputSearch.text.isEmpty()
                ) View.VISIBLE else View.GONE
        }
    }

    private fun onInit() {
        conditionUIState(UIState.INITED)
    }

    private fun onPending() {
        conditionUIState(UIState.PENDING)
    }

    private fun onSuccess(tracks: List<TrackUI>) {
        conditionUIState(state = if (tracks.isEmpty()) UIState.EMPTY else UIState.SUCCESS)
    }

    private fun onError() {
        conditionUIState(UIState.ERROR)
    }

    private fun btnCleanVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun onClickTrack(track: TrackUI, remember: Boolean) {
        val intent = Intent(this, TrackActivity::class.java)

        intent.putExtra(TRACK, Gson().toJson(track))

        startActivity(intent)

        if (remember) {
            searchHistoryViewState.add(track)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun conditionUIState(state: UIState) {
        runOnUiThread {
            when (state) {
                UIState.INITED -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    tracksRecyclerView.visibility = View.GONE
                }

                UIState.SUCCESS -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    tracksRecyclerView.visibility = View.VISIBLE
                }

                UIState.PENDING -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    tracksRecyclerView.visibility = View.GONE

                }

                UIState.EMPTY -> {
                    emptyLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    tracksRecyclerView.visibility = View.GONE
                }

                UIState.ERROR -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    tracksRecyclerView.visibility = View.GONE
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_TEXT, searchText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        super.onRestoreInstanceState(savedInstanceState)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}