package com.example.playlistmaker.sharing.domain.useCases

import android.content.Intent

open class ShareLinkUseCase {
    fun share(link: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            link
        )

        return  intent
    }
}