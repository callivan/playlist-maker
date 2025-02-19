package com.example.playlistmaker.theme.ui.activities

import android.app.Application
import com.example.playlistmaker.creator.ThemeCreator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val interactor = ThemeCreator.provideThemeInteractor(this)

        interactor.switch(interactor.get())
    }
}