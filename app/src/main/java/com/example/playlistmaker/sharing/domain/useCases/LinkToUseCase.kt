package com.example.playlistmaker.sharing.domain.useCases

import android.content.Intent
import android.net.Uri

open class LinkToUseCase {
    fun linkTo(link: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

        return intent
    }
}