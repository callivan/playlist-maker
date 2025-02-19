package com.example.playlistmaker.settings.ui.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.ThemeCreator
import com.example.playlistmaker.theme.domain.models.ThemeInteractor

class SettingsViewModel(private val themeInteractor: ThemeInteractor) : ViewModel() {
    private val themeLiveData = MutableLiveData<Boolean>(themeInteractor.get())

    fun getThemeLiveData(): LiveData<Boolean> = themeLiveData

    fun switchTheme(isDarkTheme: Boolean) {
        themeLiveData.postValue(isDarkTheme)
        themeInteractor.switch(isDarkTheme)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val context = this[APPLICATION_KEY] as Application

                    SettingsViewModel(themeInteractor = ThemeCreator.provideThemeInteractor(context))
                }
            }
    }
}