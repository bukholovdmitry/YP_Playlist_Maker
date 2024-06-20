package com.example.yp_playlist_maker.search.data.network

import com.example.yp_playlist_maker.search.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TracksSearchResponse
}