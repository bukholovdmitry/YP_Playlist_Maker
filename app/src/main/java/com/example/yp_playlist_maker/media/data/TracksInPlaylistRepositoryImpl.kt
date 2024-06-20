package com.example.yp_playlist_maker.media.data

import com.example.yp_playlist_maker.media.data.db.AppDatabase
import com.example.yp_playlist_maker.media.data.db.converters.TrackDbConvertor
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistTrackEntity
import com.example.yp_playlist_maker.media.data.db.entity.TracksInPlaylistEntity
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistRepository
import com.example.yp_playlist_maker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksInPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : TracksInPlaylistRepository {
    override suspend fun insertTrackInPlaylist(playlistId: Int, track: Track) {
        appDatabase.playlistTrackDao().insertTrack(trackDbConvertor.mapToPlaylistTrackEntity(track))
        appDatabase.tracksInPlaylistDao()
            .insertTrack(TracksInPlaylistEntity(0, playlistId, track.trackId))
    }

    override fun getTrackIdsInPlaylist(playlistId: Int): Flow<List<Int>> {
        return appDatabase.tracksInPlaylistDao().getTrackIdsInPlaylist(playlistId)
    }

    override fun getTracksInPlaylist(playlistId: Int): Flow<List<Track>> = flow {
        appDatabase.tracksInPlaylistDao().getTracksInPlaylist(playlistId).collect {
            emit(convertFromTrackEntity(it))
        }
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, track: Track) {
        appDatabase.tracksInPlaylistDao().deleteFromPlaylist(playlistId, track.trackId)
    }

    override suspend fun deleteAllTracksFromPlaylist(playlistId: Int) {
        appDatabase.tracksInPlaylistDao().deleteAllTracksFromPlaylist(playlistId)
    }

    override suspend fun deleteIfUnusableTrack(playlistId: Int, trackId: Int) {
        appDatabase.playlistTrackDao().deleteUnusableTrack(playlistId, trackId)
    }

    override suspend fun deleteIfUnusableTracks(playlistId: Int) {
        appDatabase.playlistTrackDao().deleteAllTracksFromPlaylist(playlistId)
    }

    private fun convertFromTrackEntity(tracks: List<PlaylistTrackEntity>): List<Track> {
        return tracks.map { playlistTrackEntity -> trackDbConvertor.mapToTrack(playlistTrackEntity) }
    }
}