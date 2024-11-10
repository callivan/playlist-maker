package com.example.playlistmaker

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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.iTunesAPIService.ITunesAPIService
import com.example.playlistmaker.iTunesAPIService.iTunesResponse
import com.example.playlistmaker.trackReciclerView.TrackAdapter
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val iTunesAPIService = ITunesAPIService()
        val btnBack = findViewById<MaterialToolbar>(R.id.back)
        val inputSearch = findViewById<EditText>(R.id.inputSearch)
        val btnClean = findViewById<ImageButton>(R.id.btnClean)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val emptyLayout = findViewById<LinearLayout>(R.id.empty_layout)
        val errorLayout = findViewById<LinearLayout>(R.id.error_layout)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        val conditionalViews = { props: iTunesResponse ->
            when (props.status) {
                Status.SUCCESS -> {
                    emptyLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.adapter = TrackAdapter(props.tracks)
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

        btnUpdate.setOnClickListener {
            iTunesAPIService.getTracks(
                searchText,
                conditionalViews
            )
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

            conditionalViews(iTunesResponse(Status.EMPTY, listOf()))
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClean.visibility = btnCleanVisibility(s)
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputSearch.addTextChangedListener(simpleTextWatcher)

        inputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesAPIService.getTracks(
                    searchText,
                    conditionalViews
                )
                true
            }
            false
        }
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