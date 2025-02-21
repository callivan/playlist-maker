package com.example.playlistmaker.settings.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.theme.domain.models.ThemeInteractor

class SettingsViewModel(private val themeInteractor: ThemeInteractor) : ViewModel() {
    private val themeLiveData = MutableLiveData<Boolean>(themeInteractor.get())

    fun getThemeLiveData(): LiveData<Boolean> = themeLiveData

    fun switchTheme(isDarkTheme: Boolean) {
        themeLiveData.postValue(isDarkTheme)
        themeInteractor.switch(isDarkTheme)
    }
}