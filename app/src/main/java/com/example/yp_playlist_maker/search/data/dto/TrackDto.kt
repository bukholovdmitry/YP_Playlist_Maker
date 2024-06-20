package com.example.yp_playlist_maker.search.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TrackDto(
    @SerializedName("trackId") val trackId: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("primaryGenreName") val primaryGenreName: String,
    @SerializedName("country") val country: String,
    @SerializedName("releaseDate") val releaseDate: Date,
    @SerializedName("previewUrl") val previewUrl: String

)