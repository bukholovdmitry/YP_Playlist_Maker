package com.example.yp_playlist_maker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val namePlaylistName: String,
    val descriptionPlaylist: String,
    val pathImage: String,
    var countTracksInPlaylist: Int
)
