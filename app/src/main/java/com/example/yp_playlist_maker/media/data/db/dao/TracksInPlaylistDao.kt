package com.example.yp_playlist_maker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistTrackEntity
import com.example.yp_playlist_maker.media.data.db.entity.TracksInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTrack(tracksInPlaylist: TracksInPlaylistEntity)

    @Query("SELECT trackId FROM tracks_in_playlist_table WHERE playlistId = :playlistId")
    fun getTrackIdsInPlaylist(playlistId: Int): Flow<List<Int>>

    @Query(
        "SELECT playlist_track_table.* FROM tracks_in_playlist_table " +
                "JOIN playlist_track_table " +
                "ON playlist_track_table.trackId = tracks_in_playlist_table.trackId " +
                " WHERE tracks_in_playlist_table.playlistId = :playlistId ORDER BY tracks_in_playlist_table.tracksInPlaylistId desc"
    )
    fun getTracksInPlaylist(playlistId: Int): Flow<List<PlaylistTrackEntity>>

    @Query("SELECT COUNT(*) countsInPlaylists FROM tracks_in_playlist_table WHERE trackId = :trackId")
    fun countsInPlaylists(trackId: Int): Flow<Int>

    @Query("DELETE FROM TRACKS_IN_PLAYLIST_TABLE WHERE playlistId = :playlistId and trackId = :trackId")
    suspend fun deleteFromPlaylist(playlistId: Int, trackId: Int)

    @Query("DELETE FROM TRACKS_IN_PLAYLIST_TABLE WHERE playlistId = :playlistId")
    suspend fun deleteAllTracksFromPlaylist(playlistId: Int)
}