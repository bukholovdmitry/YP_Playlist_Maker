package com.example.yp_playlist_maker.media.domain.db

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun playlists(): Flow<List<Playlist>>

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylist(playlistId: Int): Flow<Playlist>

    fun saveImageToPrivateStorage(uri: Uri): String

    fun deleteImageFromPrivateStorage(path: String)
}