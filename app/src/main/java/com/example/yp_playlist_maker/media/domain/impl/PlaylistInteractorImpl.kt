package com.example.yp_playlist_maker.media.domain.impl

import android.net.Uri
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistInteractor
import com.example.yp_playlist_maker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override fun playlists(): Flow<List<Playlist>> {
        return playlistRepository.playlists()
    }

    override suspend fun syncPlaylist(playlist: Playlist) {
        return playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override fun getPlaylist(playlistId: Int): Flow<Playlist> {
        return playlistRepository.getPlaylist(playlistId)
    }

    override fun saveImageToPrivateStorage(uri: Uri): String {
        return playlistRepository.saveImageToPrivateStorage(uri)
    }

    override fun deleteImageFromPrivateStorage(path: String) {
        playlistRepository.deleteImageFromPrivateStorage(path)
    }
}