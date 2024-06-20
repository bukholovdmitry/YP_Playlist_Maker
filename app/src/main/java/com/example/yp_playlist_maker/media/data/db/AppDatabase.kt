package com.example.yp_playlist_maker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.yp_playlist_maker.media.data.db.converters.Converters
import com.example.yp_playlist_maker.media.data.db.dao.PlaylistDao
import com.example.yp_playlist_maker.media.data.db.dao.PlaylistTrackDao
import com.example.yp_playlist_maker.media.data.db.dao.TrackDao
import com.example.yp_playlist_maker.media.data.db.dao.TracksInPlaylistDao
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistEntity
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistTrackEntity
import com.example.yp_playlist_maker.media.data.db.entity.TrackEntity
import com.example.yp_playlist_maker.media.data.db.entity.TracksInPlaylistEntity


@Database(
    version = 2, entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TracksInPlaylistEntity::class,
        PlaylistTrackEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun tracksInPlaylistDao(): TracksInPlaylistDao

    abstract fun playlistTrackDao(): PlaylistTrackDao
}