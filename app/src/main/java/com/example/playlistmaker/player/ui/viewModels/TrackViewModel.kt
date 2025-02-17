package com.example.playlistmaker.player.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.Player
import com.example.playlistmaker.player.domain.models.PlayerScreenState
import com.example.playlistmaker.player.domain.useCases.GetTrackUseCases
import com.example.playlistmaker.player.ui.activities.TrackActivity
import com.example.playlistmaker.player.ui.models.TrackUI

class TrackViewModel(
    activity: TrackActivity,
    private val trackUseCase: GetTrackUseCases,
    private val player: Player,
) : ViewModel() {
    private val trackLiveData = MutableLiveData<TrackUI>()

    fun getPlayerScreenStateLiveData(): LiveData<PlayerScreenState> =
        player.getPlayerScreenStateLiveData()

    fun getPlayerProgressLiveData(): LiveData<PlayerState> = player.getPlayerProgressLiveData()
    fun getTrackLiveData(): LiveData<TrackUI> = trackLiveData

    init {
        val track = trackUseCase.getTrackFromIntent(activity)

        player.prepare(track.previewUrl)

        trackLiveData.postValue(
            TrackUI(
                country = track.country,
                trackId = track.trackId,
                trackName = track.trackName,
                previewUrl = track.previewUrl,
                artistName = track.artistName,
                releaseDate = track.releaseDate,
                artworkUrl100 = track.artworkUrl100,
                collectionName = track.collectionName,
                trackTimeMillis = track.trackTimeMillis,
                primaryGenreName = track.primaryGenreName,
            )
        )
    }

    fun pause() {
        player.pause()
    }

    fun release() {
        player.release()
    }

    fun playerController(onPlay: () -> Unit, onPause: () -> Unit) {
        if (getPlayerProgressLiveData().value!!.isPlaying) {
            player.pause()
            onPause()
        } else {
            player.play()
            onPlay()
        }
    }

    override fun onCleared() {
        player.release()
    }

    companion object {
        fun getViewModelFactory(
            activity: TrackActivity,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TrackViewModel(
                    activity = activity,
                    player = Player(),
                    trackUseCase = GetTrackUseCases(),
                )
            }
        }
    }
}