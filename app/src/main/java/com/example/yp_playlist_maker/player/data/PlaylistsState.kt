package com.example.yp_playlist_maker.player.data

import com.example.yp_playlist_maker.media.domain.db.Playlist

sealed interface PlaylistsState {
    data class StateData(val playlists: List<Playlist>) : PlaylistsState
    data class StateMakeMessage(val message: String) : PlaylistsState
}