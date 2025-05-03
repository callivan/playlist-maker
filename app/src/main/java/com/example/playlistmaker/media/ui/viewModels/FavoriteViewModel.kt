package com.example.playlistmaker.media.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.media.ui.models.FavoriteScreenState
import com.example.playlistmaker.media.ui.models.TrackUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor,
) : ViewModel() {

    private val favoriteSearchScreenStateLiveData =
        MutableLiveData<FavoriteScreenState>(FavoriteScreenState.Loading)

    fun getFavoriteScreenStateLiveData(): LiveData<FavoriteScreenState> =
        favoriteSearchScreenStateLiveData

    fun getFavoriteTracks() {
        favoriteSearchScreenStateLiveData.postValue(FavoriteScreenState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.getFavoriteTracks().collect { tracks ->
                if (tracks.isNotEmpty()) {
                    favoriteSearchScreenStateLiveData.postValue(FavoriteScreenState.Content(tracks.map { track ->
                        TrackUI(
                            id = track.id,
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
                    }))
                } else {
                    favoriteSearchScreenStateLiveData.postValue(FavoriteScreenState.Empty)
                }
            }
        }
    }
}