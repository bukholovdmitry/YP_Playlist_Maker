package com.example.yp_playlist_maker.player.data

import com.example.yp_playlist_maker.media.data.db.AppDatabase
import com.example.yp_playlist_maker.media.data.db.converters.TrackDbConvertor
import com.example.yp_playlist_maker.media.data.db.entity.TrackEntity
import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksRepository
import com.example.yp_playlist_maker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteTracksRepository {
    override fun likedTracks(): Flow<List<Track>> = flow {
        appDatabase.trackDao().getTracks().collect {
            emit(convertFromTrackEntity(it))
        }
    }

    override suspend fun likeTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.mapToTrackEntity(track))
    }

    override suspend fun unlikeTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.mapToTrackEntity(track))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { trackEntity -> trackDbConvertor.mapToTrack(trackEntity) }
    }
}