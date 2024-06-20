package com.example.yp_playlist_maker.media.domain.impl

import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistInteractor
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistRepository
import com.example.yp_playlist_maker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class TracksInPlaylistInteractorImpl(private val tracksInPlaylistRepository: TracksInPlaylistRepository) :
    TracksInPlaylistInteractor {
    override suspend fun insertTrackInPlaylist(playlistId: Int, track: Track) {
        tracksInPlaylistRepository.insertTrackInPlaylist(playlistId, track)
    }

    override fun getTrackIdsInPlaylist(playlistId: Int): Flow<List<Int>> {
        return tracksInPlaylistRepository.getTrackIdsInPlaylist(playlistId)
    }

    override fun getTracksInPlaylist(playlistId: Int): Flow<List<Track>> {
        return tracksInPlaylistRepository.getTracksInPlaylist(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, track: Track) {
        return tracksInPlaylistRepository.deleteTrackFromPlaylist(playlistId, track)
    }

    override suspend fun deleteAllTracksFromPlaylist(playlistId: Int) {
        tracksInPlaylistRepository.deleteAllTracksFromPlaylist(playlistId)
    }

    override suspend fun deleteIfUnusableTrack(playlistId: Int, trackId: Int) {
        tracksInPlaylistRepository.deleteIfUnusableTrack(playlistId, trackId)
    }

    override suspend fun deleteIfUnusableTracks(playlistId: Int) {
        tracksInPlaylistRepository.deleteIfUnusableTracks(playlistId)
    }
}