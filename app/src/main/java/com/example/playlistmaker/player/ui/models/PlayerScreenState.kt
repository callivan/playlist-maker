package com.example.playlistmaker.player.ui.models

sealed class PlayerScreenState {
    object Pending : PlayerScreenState()
    object Prepared : PlayerScreenState()
    data class Playing(val progress: Int) : PlayerScreenState()
    object Paused : PlayerScreenState()
    object Released : PlayerScreenState()
    object Completed : PlayerScreenState()
}