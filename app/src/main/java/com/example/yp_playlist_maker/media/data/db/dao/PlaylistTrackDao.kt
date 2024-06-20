package com.example.yp_playlist_maker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistTrackEntity)

    @Delete
    suspend fun deleteTrack(track: PlaylistTrackEntity)

    @Query(
        "delete from playlist_track_table " +
                "where trackId = :trackId and trackId not in (select trackId from tracks_in_playlist_table where playlistId <> :playlistId) "
    )
    suspend fun deleteUnusableTrack(playlistId: Int, trackId: Int)

    @Query(
        "delete from playlist_track_table " +
                "where trackId in (select trackId from tracks_in_playlist_table where playlistId = :playlistId) and trackId not in (select trackId from tracks_in_playlist_table where playlistId <> :playlistId) "
    )
    suspend fun deleteAllTracksFromPlaylist(playlistId: Int)
}