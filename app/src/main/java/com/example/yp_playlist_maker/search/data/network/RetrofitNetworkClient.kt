package com.example.yp_playlist_maker.search.data.network

import android.util.Log
import com.example.yp_playlist_maker.search.data.NetworkClient
import com.example.yp_playlist_maker.search.data.dto.Response
import com.example.yp_playlist_maker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return try {
            if (dto is TracksSearchRequest) {
                withContext(Dispatchers.IO) {
                    try {
                        val resp = iTunesService.search(dto.expression)
                        resp.apply { resultCode = 200 }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }

                }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (e: Exception) {
            Log.d("ERROR", "400")
            Response().apply { resultCode = 400 }
        }
    }


}