package com.example.yp_playlist_maker.search.repository


import com.example.yp_playlist_maker.search.data.NetworkClient
import com.example.yp_playlist_maker.search.data.dto.TracksSearchRequest
import com.example.yp_playlist_maker.search.data.dto.TracksSearchResponse
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.domain.TracksRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

const val CONNECTION_SUCCESS = 200

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Result<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            CONNECTION_SUCCESS -> {

                with(response as TracksSearchResponse) {
                    val data = results.map {
                        val calendar = Calendar.getInstance()
                        calendar.time = it.releaseDate

                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTime,
                            it.artworkUrl100,
                            it.collectionName,
                            it.primaryGenreName,
                            it.country,
                            calendar.get(Calendar.YEAR),
                            it.previewUrl,
                            false
                        )
                    }
                    emit(Result.success(ArrayList(data)))
                }
            }

            else -> {
                emit(Result.failure(exception = Throwable()))
            }
        }
    }

    override fun loadTrackData(expression: String): Track {
        return Gson().fromJson<Track>(expression, object : TypeToken<Track>() {}.type)
    }
}
