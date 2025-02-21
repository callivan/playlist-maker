package com.example.playlistmaker.player.ui.viewModels

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.ui.models.PlayerScreenState
import com.example.playlistmaker.utils.Utils

class TrackViewModel() : ViewModel() {
    private val player = MediaPlayer()

    private var removeDebounce: (() -> Unit)? = null

    private fun setProgress() {
        val debouncePlayerProgressWithThread = Utils.debounceWithThread({
            playerScreenStateLiveData.postValue(PlayerScreenState.Playing(progress = player.currentPosition))
            setProgress()
        }, 300L)

        debouncePlayerProgressWithThread.debounce()

        removeDebounce = debouncePlayerProgressWithThread.remove
    }

    private val playerScreenStateLiveData =
        MutableLiveData<PlayerScreenState>(PlayerScreenState.Pending)

    fun getPlayerScreenStateLiveData(): LiveData<PlayerScreenState> =
        playerScreenStateLiveData

    fun play() {
        player.start()
        setProgress()
    }

    fun pause() {
        player.pause()
        removeDebounce?.invoke()
        playerScreenStateLiveData.postValue(PlayerScreenState.Paused)
    }

    fun release() {
        player.release()
        removeDebounce?.invoke()
        playerScreenStateLiveData.postValue(PlayerScreenState.Released)
    }

    fun playbackController() {
        if (getPlayerScreenStateLiveData().value is PlayerScreenState.Playing) {
            pause()
        } else {
            play()
        }
    }

    fun prepare(url: String) {
        player.setDataSource(url)
        player.prepareAsync()

        player.setOnPreparedListener {
            playerScreenStateLiveData.postValue(PlayerScreenState.Prepared)
        }

        player.setOnCompletionListener {
            playerScreenStateLiveData.postValue(PlayerScreenState.Completed)
            removeDebounce?.invoke()
        }
    }

    override fun onCleared() {
        release()
    }
}