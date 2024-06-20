package com.example.yp_playlist_maker.player.domain.impl

import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksInteractor
import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksRepository
import com.example.yp_playlist_maker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override fun likedTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.likedTracks()
    }

    override suspend fun likeTrack(track: Track) {
        favoriteTracksRepository.likeTrack(track)
    }

    override suspend fun unlikeTrack(track: Track) {
        favoriteTracksRepository.unlikeTrack(track)
    }
}