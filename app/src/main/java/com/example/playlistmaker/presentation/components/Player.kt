package com.example.playlistmaker.presentation.components

import android.media.MediaPlayer
import com.example.playlistmaker.presentation.models.PlayerState
import com.example.playlistmaker.utils.Utils

class Player {
    private val player = MediaPlayer()
    private var playerStatus = PlayerState.DEFAULT
    private var removeDebounce: (() -> Unit)? = null

    fun startPlayer() {
        player.start()
        playerStatus = PlayerState.PLAYING
    }

    fun pausePlayer() {
        if (removeDebounce != null) {
            player.pause()
            playerStatus = PlayerState.PAUSED
            removeDebounce?.invoke()
        }
    }

    fun releasePlayer() {
        if (removeDebounce != null) {
            player.release()
            playerStatus = PlayerState.DEFAULT
            removeDebounce?.invoke()
        }
    }

    fun preparePlayer(url: String, onPrepared: () -> Unit, onComplete: () -> Unit) {
        player.setDataSource(url)
        player.prepareAsync()

        player.setOnPreparedListener {
            onPrepared()
            playerStatus = PlayerState.PREPARED
        }

        player.setOnCompletionListener {
            onComplete()
            removeDebounce?.invoke()
        }
    }

    fun playbackControl(onStart: () -> Unit = {}, onPause: () -> Unit = {}) {
        if (playerStatus == PlayerState.PLAYING) {
            pausePlayer()
            onPause()
        } else if (playerStatus == PlayerState.PREPARED || playerStatus == PlayerState.PAUSED) {
            startPlayer()
            onStart()
        }
    }

    fun getPosition(cb: (position: Int) -> Unit) {
        val debounceWithThread = Utils.debounceWithThread({
            cb(player.currentPosition)
            this.getPosition(cb)
        }, 300L)

        debounceWithThread.debounce()

        removeDebounce = debounceWithThread.remove
    }
}