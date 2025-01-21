package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
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
import com.example.playlistmaker.iTunesAPIService.ITunesAPIService
import com.example.playlistmaker.iTunesAPIService.iTunesAPITrack
import com.example.playlistmaker.iTunesAPIService.iTunesResponse
import com.example.playlistmaker.searchHistory.SearchHistoryAdapter
import com.example.playlistmaker.searchHistory.SearchHistoryService
import com.example.playlistmaker.trackReciclerView.TrackAdapter
import com.example.playlistmaker.utils.Utils
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson

const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"

const val TRACK = "track"

class SearchActivity : AppCompatActivity() {
    private var searchText = ""

    private lateinit var inputSearch: EditText

    private lateinit var progressBar: ProgressBar

    private lateinit var btnBack: MaterialToolbar
    private lateinit var btnClean: ImageButton
    private lateinit var btnUpdate: Button
    private lateinit var btnCleanSearchHistory: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchHistoryRecyclerView: RecyclerView

    private lateinit var emptyLayout: LinearLayout
    private lateinit var errorLayout: LinearLayout
    private lateinit var searchHistoryLayout: LinearLayout

    private val iTunesAPIService = ITunesAPIService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPrefs = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)

        val searchHistory = SearchHistoryService(sharedPrefs, ::searchHistoryListUpdated)

        inputSearch = findViewById(R.id.inputSearch)

        progressBar = findViewById(R.id.progressBar)

        btnBack = findViewById(R.id.back)
        btnClean = findViewById(R.id.btnClean)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnCleanSearchHistory = findViewById(R.id.btnCleanHistory)

        searchHistoryRecyclerView = findViewById(R.id.recyclerViewHistorySearch)
        recyclerView = findViewById(R.id.recyclerViewTracks)

        val searchHistoryList = searchHistory.get()

        emptyLayout = findViewById(R.id.empty_layout)
        errorLayout = findViewById(R.id.error_layout)
        searchHistoryLayout = findViewById(R.id.searchHistory)

        fun onClickTrack(track: iTunesAPITrack, remember: Boolean) {
            val intent = Intent(this, TrackActivity::class.java)

            intent.putExtra(TRACK, Gson().toJson(track))

            startActivity(intent)

            if (remember) {
                searchHistory.add(track)
            }
        }

        searchHistoryRecyclerView.adapter = SearchHistoryAdapter(searchHistoryList, ::onClickTrack)


        fun conditionalViews(props: iTunesResponse) {
            when (props.status) {
                Status.INITED -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }

                Status.SUCCESS -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.adapter =
                        TrackAdapter(props.tracks, ::onClickTrack)
                }

                Status.PENDING -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    recyclerView.visibility = View.GONE

                }

                Status.EMPTY -> {
                    emptyLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }

                Status.ERROR -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        }

        fun getTracks() {
            iTunesAPIService.getTracks(
                searchText,
                ::conditionalViews
            )
        }

        val debounceSearchTrack = Utils.debounceWithThread(::getTracks, 1000L)

        btnUpdate.setOnClickListener {
            getTracks()
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

            conditionalViews(iTunesResponse(Status.INITED, listOf()))
        }

        btnCleanSearchHistory.setOnClickListener {
            searchHistory.clean()
            searchHistoryLayout.visibility = View.GONE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClean.visibility = btnCleanVisibility(s)

                searchText = s.toString()

                searchHistoryLayout.visibility =
                    if (inputSearch.hasFocus() && searchHistoryList.isNotEmpty() && s?.isEmpty() == true
                    ) View.VISIBLE else View.GONE

                debounceSearchTrack()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputSearch.addTextChangedListener(simpleTextWatcher)

        inputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTracks()
                true
            }
            false
        }

        inputSearch.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.visibility =
                if (hasFocus && searchHistoryList.isNotEmpty() && inputSearch.text.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun searchHistoryListUpdated() {
        searchHistoryRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun btnCleanVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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