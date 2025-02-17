package com.example.playlistmaker.settings.ui.viewModels

import android.app.Activity.MODE_PRIVATE
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.settings.domain.useCases.LinkToUseCase
import com.example.playlistmaker.settings.domain.useCases.SendEmailUseCase
import com.example.playlistmaker.settings.domain.useCases.ShareLinkUseCase
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.settings.domain.useCases.SwitchThemeUseCase
import com.example.playlistmaker.settings.ui.activities.SettingsActivity

class SettingsViewModel(
    private val activity: SettingsActivity,
    private val linkToUseCase: LinkToUseCase,
    private val sendEmailUseCase: SendEmailUseCase,
    private val shareLinkUseCase: ShareLinkUseCase,
    private val switchThemeUseCase: SwitchThemeUseCase
) : ViewModel() {

    private val sharedPrefs = activity.getSharedPreferences(Const.THEME_PREFS, MODE_PRIVATE)


    fun isDarkTheme(): Boolean = sharedPrefs.getBoolean(Const.THEME, false)

    fun linTo(link: String) {
        activity.startActivity(linkToUseCase.linkTo(link))
    }

    fun sendEmail(mail: String, title: String, text: String) {
        activity.startActivity(sendEmailUseCase.send(mail = mail, title = title, text = text))
    }

    fun shareLink(link: String) {
        activity.startActivity(
            Intent.createChooser(
                shareLinkUseCase.share(link),
                "Share link"
            )
        )
    }

    fun switchTheme(checked: Boolean) {
        switchThemeUseCase.switchTheme(checked)
        sharedPrefs.edit().putBoolean(Const.THEME, checked).apply()
    }


    companion object {
        fun getViewModelFactory(activity: SettingsActivity): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(
                        activity = activity,
                        linkToUseCase = LinkToUseCase(),
                        sendEmailUseCase = SendEmailUseCase(),
                        shareLinkUseCase = ShareLinkUseCase(),
                        switchThemeUseCase = SwitchThemeUseCase()
                    )
                }
            }
    }
}