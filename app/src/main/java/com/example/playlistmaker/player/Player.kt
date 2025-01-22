package com.example.playlistmaker.player

import android.media.MediaPlayer
import com.example.playlistmaker.utils.Utils

class Player {
    private val player = MediaPlayer()
    private var playerStatus = Status.DEFAULT
    private lateinit var removeDebounce: () -> Unit

    fun startPlayer() {
        player.start()
        playerStatus = Status.PLAYING
    }

    fun pausePlayer() {
        player.pause()
        playerStatus = Status.PAUSED
        removeDebounce()
    }

    fun releasePlayer() {
        player.release()
        playerStatus = Status.DEFAULT
        removeDebounce()
    }

    fun preparePlayer(url: String, onPrepared: () -> Unit, onComplete: () -> Unit) {
        player.setDataSource(url)
        player.prepareAsync()

        player.setOnPreparedListener {
            onPrepared()
            playerStatus = Status.PREPARED
        }

        player.setOnCompletionListener {
            onComplete()
            removeDebounce()
        }
    }

    fun playbackControl(onStart: () -> Unit = {}, onPause: () -> Unit = {}) {
        if (playerStatus == Status.PLAYING) {
            pausePlayer()
            onPause()
        } else if (playerStatus == Status.PREPARED || playerStatus == Status.PAUSED) {
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