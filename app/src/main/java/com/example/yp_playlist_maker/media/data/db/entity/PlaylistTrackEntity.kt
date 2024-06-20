package com.example.yp_playlist_maker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "playlist_track_table")
data class PlaylistTrackEntity(
    @PrimaryKey
    val trackId: Int,
    val artworkUrl100: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: Int,
    val primaryGenreName: String,
    val country: String,
    val trackTime: Int,
    val previewUrl: String,
    val changeDate: Date?,
)