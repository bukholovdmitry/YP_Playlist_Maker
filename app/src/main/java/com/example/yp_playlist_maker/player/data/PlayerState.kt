package com.example.yp_playlist_maker.player.data

import com.example.yp_playlist_maker.search.domain.Track

sealed interface PlayerState {

    data class StateDefault(val track: Track) : PlayerState
    data class StatePlaying(val currentPosition: String) : PlayerState
    data object Pause : PlayerState
    data object Prepared : PlayerState
    data class Complete(val startPosition: String) : PlayerState
    data class Resume(val track: Track, val currentPosition: String) : PlayerState
}