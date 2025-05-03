package com.example.playlistmaker.utils

import android.text.Editable
import android.text.TextWatcher

class CustomTextWatcher {
    companion object {
        fun customTextWatcher(onTextChanged: (s: CharSequence?) -> Unit): TextWatcher {
            return object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // empty
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onTextChanged(s)
                }

                override fun afterTextChanged(s: Editable?) {
                    // empty
                }
            }
        }
    }
}