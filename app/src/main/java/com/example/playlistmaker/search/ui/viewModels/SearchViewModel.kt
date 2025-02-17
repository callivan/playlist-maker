package com.example.playlistmaker.search.ui.viewModels

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.SearchTracksCreator
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.search.data.sharedPrefs.TracksHistorySharedPrefs
import com.example.playlistmaker.search.domain.models.TracksInteractor
import com.example.playlistmaker.search.ui.activities.SearchActivity
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.utils.Utils

class SearchViewModel(
    activity: SearchActivity,
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    private val sharedPrefs =
        activity.getSharedPreferences(Const.SEARCH_HISTORY_PREFS, MODE_PRIVATE)

    private var searchHistory = TracksHistorySharedPrefs(sharedPrefs)

    private val searchScreenStateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.Init)
    private val inputTextLivData = MutableLiveData<String>("")

    private val debouncer =
        Utils.debounceWithThread({ getTracks(getInputTextLiveData().value) }, 1000L)

    fun resetSearchScreenStateLiveData(): Unit =
        searchScreenStateLiveData.postValue(SearchScreenState.Init)

    fun resetInputTextLiveData(): Unit = inputTextLivData.postValue("")


    fun getSearchScreenStateLiveData(): LiveData<SearchScreenState> = searchScreenStateLiveData
    fun getInputTextLiveData(): LiveData<String> = inputTextLivData

    fun getTracks(text: String? = null) {
        tracksInteractor.getTracks(text = (text ?: getInputTextLiveData()).toString(), onPending = {
            searchScreenStateLiveData.postValue(SearchScreenState.Pending)
        }, onSuccess = { tracks ->

            if (tracks.isNotEmpty()) {
                searchScreenStateLiveData.postValue(SearchScreenState.Content(tracks.map {
                    TrackUI(
                        country = it.country,
                        trackId = it.trackId,
                        trackName = it.trackName,
                        previewUrl = it.previewUrl,
                        artistName = it.artistName,
                        releaseDate = it.releaseDate,
                        artworkUrl100 = it.artworkUrl100,
                        collectionName = it.collectionName,
                        trackTimeMillis = it.trackTimeMillis,
                        primaryGenreName = it.primaryGenreName,
                    )
                }))
            } else {
                searchScreenStateLiveData.postValue(SearchScreenState.Empty)
            }
        }, onError = {
            searchScreenStateLiveData.postValue(SearchScreenState.Error)
        })
    }

    fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputTextLivData.postValue(s.toString())

                if (s.toString().isEmpty()) {
                    debouncer.remove()
                } else {
                    debouncer.debounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
    }

    fun getSearchHistory(): List<TrackUI> {
        return searchHistory.get()
    }

    fun addInSearchHistory(track: TrackUI) {
        searchHistory.add(track)
    }

    fun cleanSearchHistory() {
        searchHistory.clean()
    }

    fun cleanInputTextLiveData() = inputTextLivData.postValue("")

    companion object {
        fun getViewModelFactory(
            activity: SearchActivity,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    activity = activity,
                    tracksInteractor = SearchTracksCreator.provideTracksInteractor(),
                )
            }
        }
    }
}