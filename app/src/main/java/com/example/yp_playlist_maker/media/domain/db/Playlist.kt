package com.example.yp_playlist_maker.media.domain.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String,
    val pathImage: String,
    var countTracksInPlaylist: Int
) : Parcelable