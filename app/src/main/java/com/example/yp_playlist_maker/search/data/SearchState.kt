package com.example.yp_playlist_maker.search.data

import android.view.View.OnClickListener
import com.example.yp_playlist_maker.search.domain.Track

sealed interface SearchState {
    data object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class Error(
        val onClickListener: OnClickListener
    ) : SearchState

    data object Empty : SearchState

    data class History(val tracks: List<Track>) : SearchState
}