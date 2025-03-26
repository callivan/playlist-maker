package com.example.playlistmaker.player.ui.viewModels

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.ui.models.PlayerScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackViewModel() : ViewModel() {
    private val player = MediaPlayer()

    private var timerJob: Job? = null

    private fun setProgress() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying) {
                delay(300L)
                playerScreenStateLiveData.postValue(PlayerScreenState.Playing(progress = player.currentPosition))
            }
        }
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
        timerJob?.cancel()
        playerScreenStateLiveData.postValue(PlayerScreenState.Paused)
    }

    fun release() {
        player.release()
        timerJob?.cancel()
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
            timerJob?.cancel()
        }
    }

    override fun onCleared() {
        release()
    }
}