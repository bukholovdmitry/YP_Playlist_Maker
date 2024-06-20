package com.example.yp_playlist_maker.media.data

import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.search.domain.Track


data class PlaylistInfoState(val playlist: Playlist, val tracks: List<Track>)