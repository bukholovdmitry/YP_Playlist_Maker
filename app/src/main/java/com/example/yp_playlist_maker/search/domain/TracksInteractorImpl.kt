package com.example.yp_playlist_maker.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map {
            Pair(it.getOrNull(), null)
        }
    }

    override fun loadTrackData(expression: String): Track {
        return repository.loadTrackData(expression)
    }
}