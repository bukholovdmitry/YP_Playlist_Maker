package com.example.yp_playlist_maker.search.view_model

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.search.data.SearchState
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractor
import com.example.yp_playlist_maker.search.domain.TracksInteractor
import com.example.yp_playlist_maker.utils.debounce
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class SearchViewModel : ViewModel() {
    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler: Handler by inject(Handler::class.java)
    private val tracksInteractor: TracksInteractor by inject(TracksInteractor::class.java)
    private val tracksHistoryInteractor: TracksHistoryInteractor by inject(TracksHistoryInteractor::class.java)
    private var lastSearchQuery = ""
    private val stateLiveData = MutableLiveData<SearchState>()

    fun observeState(): LiveData<SearchState> = stateLiveData

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) {
            searchTracks(it)
        }

    fun searchDebounce(changeText: String) {
        trackSearchDebounce(changeText)
    }

    fun searchTracks(textTrack: String) {
        Log.d("SearchActivity", "Search query: {%s}".format(textTrack))
        renderState(SearchState.Loading)

        viewModelScope.launch {
            tracksInteractor
                .searchTracks(expression = textTrack)
                .collect { pair ->
                    if (pair.second?.isEmpty() == true) {
                        renderState(SearchState.Error { searchTracks(lastSearchQuery) })
                    } else if (!pair.first.isNullOrEmpty()) {
                        renderState(SearchState.Content(pair.first ?: arrayListOf()))
                    } else {
                        renderState(SearchState.Empty)
                    }
                }
        }
    }

    val onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
        tracksHistoryInteractor.write(it)
        if (stateLiveData.value is SearchState.History) {
            showTracksHistory()
        }
    }

    fun showTracksHistory() {
        stateLiveData.postValue(SearchState.History(tracksHistoryInteractor.read().reversed()))
    }

    fun clearTracksHistory() {
        tracksHistoryInteractor.clearSavedTracks()
    }

    private fun renderState(searchState: SearchState) {
        stateLiveData.postValue(searchState)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}