package com.example.playlistmaker.player.domain.models

sealed class PlayerScreenState {
    object Prepeared : PlayerScreenState()
    object Completed : PlayerScreenState()
}