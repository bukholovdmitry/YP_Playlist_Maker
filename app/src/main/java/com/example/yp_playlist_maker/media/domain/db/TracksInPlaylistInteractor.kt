package com.example.yp_playlist_maker.media.domain.db

import com.example.yp_playlist_maker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface TracksInPlaylistInteractor {
    suspend fun insertTrackInPlaylist(playlistId: Int, track: Track)

    fun getTrackIdsInPlaylist(playlistId: Int): Flow<List<Int>>

    fun getTracksInPlaylist(playlistId: Int): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(playlistId: Int, track: Track)

    suspend fun deleteAllTracksFromPlaylist(playlistId: Int)

    suspend fun deleteIfUnusableTrack(playlistId: Int, trackId: Int)

    suspend fun deleteIfUnusableTracks(playlistId: Int)
}