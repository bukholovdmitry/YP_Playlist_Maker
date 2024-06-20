package com.example.yp_playlist_maker.search.domain

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository) :
    TracksHistoryInteractor {
    override fun clearSavedTracks() {
        repository.clearSavedTracks()
    }

    override fun read(): List<Track> {
        return repository.read()
    }

    override fun write(track: Track) {
        repository.write(track)
    }
}