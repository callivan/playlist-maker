package com.example.playlistmaker.utils

import android.util.TypedValue
import android.view.View
import com.google.android.material.snackbar.Snackbar

class CustomSnackbar {
    companion object {
        fun createSnackbar(
            view: View,
            text: String,
            time: Int = Snackbar.LENGTH_SHORT
        ): Snackbar {
            val snackbar = Snackbar.make(view, text, time)

            val typedValue = TypedValue()
            val theme = view.context.theme

            theme.resolveAttribute(
                com.google.android.material.R.attr.colorSecondary,
                typedValue,
                true
            )

            snackbar.setTextColor(typedValue.data)

            theme.resolveAttribute(
                com.google.android.material.R.attr.colorSecondaryVariant,
                typedValue,
                true
            )

            snackbar.view.setBackgroundColor(typedValue.data)

            return snackbar
        }
    }
}