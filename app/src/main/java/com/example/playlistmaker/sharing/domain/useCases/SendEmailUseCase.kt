package com.example.playlistmaker.sharing.domain.useCases

import android.content.Intent
import android.net.Uri

open class SendEmailUseCase {
    fun send(mail: String, title: String, text: String): Intent {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            title
        )
        intent.putExtra(
            Intent.EXTRA_TEXT,
            text
        )

        return intent
    }
}