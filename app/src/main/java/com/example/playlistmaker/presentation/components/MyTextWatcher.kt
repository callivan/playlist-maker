package com.example.playlistmaker.presentation.components

import android.text.Editable
import android.text.TextWatcher

class MyTextWatcher(val onTextChanged: (s: CharSequence?) -> Unit) :
    TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // empty
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged(s)
    }

    override fun afterTextChanged(s: Editable?) {
        // empty
    }
}