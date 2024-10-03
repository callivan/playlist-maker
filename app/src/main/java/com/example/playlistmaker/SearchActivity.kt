package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<MaterialToolbar>(R.id.back)
        val inputSearch = findViewById<EditText>(R.id.inputSearch)
        val btnClean = findViewById<ImageButton>(R.id.btnClean)

        btnBack.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        btnClean.visibility = btnCleanVisibility(inputSearch.text)

        btnClean.setOnClickListener {
            inputSearch.setText("")

            val view: View? = this.currentFocus

            if (view != null) {
                hideKeyboard(view)
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClean.visibility = btnCleanVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputSearch.addTextChangedListener(simpleTextWatcher)
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
}