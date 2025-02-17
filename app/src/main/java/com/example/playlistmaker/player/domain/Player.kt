package com.example.playlistmaker.player.domain

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.player.domain.models.PlayerScreenState
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.utils.Utils
import kotlin.reflect.KProperty0

class Player {
    private val player = MediaPlayer()

    private val playerStateLiveData =
        MutableLiveData<PlayerState>(PlayerState(isPlaying = false, progress = 0))

    private val playerSceerStateLiveData = MutableLiveData<PlayerScreenState>()

    private var removeDebouncer: (() -> Unit)? = null

    fun getPlayerProgressLiveData(): LiveData<PlayerState> = playerStateLiveData
    fun getPlayerScreenStateLiveData(): LiveData<PlayerScreenState> = playerSceerStateLiveData

    private fun setProgress() {
        val debounceWithThread = Utils.debounceWithThread({
            playerStateLiveData.postValue(getCurrentPlayerState().copy(progress = player.currentPosition))

            setProgress()
        }, 300L)

        debounceWithThread.debounce()

        removeDebouncer = debounceWithThread.remove
    }

    private fun getCurrentPlayerState(): PlayerState {
        return playerStateLiveData.value ?: PlayerState(progress = 0, isPlaying = false)
    }

    fun play() {
        player.start()

        setProgress()

        playerStateLiveData.postValue(getCurrentPlayerState().copy(isPlaying = true))
    }

    fun pause() {
        player.pause()

        playerStateLiveData.postValue(getCurrentPlayerState().copy(isPlaying = false))

        removeDebouncer?.invoke()

    }

    fun release() {
        player.release()

        playerStateLiveData.postValue(getCurrentPlayerState().copy(isPlaying = false))

        removeDebouncer?.invoke()
    }

    fun prepare(url: String) {
        player.setDataSource(url)
        player.prepareAsync()

        player.setOnPreparedListener {
            playerSceerStateLiveData.postValue(PlayerScreenState.Prepeared)
        }

        player.setOnCompletionListener {
            playerSceerStateLiveData.postValue(PlayerScreenState.Completed)

            removeDebouncer?.invoke()
        }
    }
}