package com.example.yp_playlist_maker.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksRepository
import com.example.yp_playlist_maker.search.domain.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val likedTracksRepository: FavoriteTracksRepository) :
    ViewModel() {
    private val stateLiveData = MutableLiveData<Result<List<Track>>>()
    fun observeState(): LiveData<Result<List<Track>>> = stateLiveData

    private fun renderState(state: Result<List<Track>>) {
        stateLiveData.postValue(state)
    }

    fun fillData() {
        viewModelScope.launch {
            likedTracksRepository
                .likedTracks()
                .collect { tracks ->
                    renderState(Result.success(tracks))
                }
        }
    }
}