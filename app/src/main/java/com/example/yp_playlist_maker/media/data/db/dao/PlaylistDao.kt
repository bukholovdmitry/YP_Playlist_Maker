package com.example.yp_playlist_maker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query(
        "select a.playlistId, a.namePlaylistName, a.descriptionPlaylist, a.pathImage, count(b.trackId) countTracksInPlaylist from playlist_table a " +
                "left join tracks_in_playlist_table b on a.playlistId = b.playlistId " +
                "group by a.playlistId order by a.playlistId"
    )
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    fun getPlaylist(playlistId: Int): Flow<PlaylistEntity>
}