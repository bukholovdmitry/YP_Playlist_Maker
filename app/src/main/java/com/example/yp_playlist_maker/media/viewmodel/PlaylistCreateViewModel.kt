package com.example.yp_playlist_maker.media.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.launch


class PlaylistCreateViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {
    private lateinit var savedInstancePlaylist: Playlist
    private var playlistIdFromArgument: Int = 0
    private var pathToSave = ""
    private var isCreatingMode = true


    private val statePlaylistLiveData = MutableLiveData<Playlist>()

    fun observeState(): LiveData<Playlist> = statePlaylistLiveData

    private fun renderState(playlist: Playlist) {
        statePlaylistLiveData.postValue(playlist)
    }

    fun createPlaylist(playlist: Playlist) {
        if (playlist.pathImage.isNotBlank()) {
            pathToSave = playlistRepository.saveImageToPrivateStorage(Uri.parse(playlist.pathImage))
        }
        val playlistToSave = Playlist(
            0,
            playlist.playlistName,
            playlist.playlistDescription,
            pathToSave,
            0
        )

        viewModelScope.launch {
            playlistRepository.insertPlaylist(playlistToSave)
        }
    }

    fun savePlaylistInstanceState(
        playistName: String,
        playlistDescription: String,
        newPath: String
    ) {
        savedInstancePlaylist =
            Playlist(playlistIdFromArgument, playistName, playlistDescription, newPath, 0)
    }

    fun loadFromSavedInstanceState() {
        renderState(savedInstancePlaylist)
    }

    fun loadFromArgument(playlist: Playlist) {
        playlistIdFromArgument = playlist.playlistId
        pathToSave = playlist.pathImage
        isCreatingMode = false

        renderState(playlist)
    }

    fun updatePlaylist(playlistName: String, playlistDescription: String, newPath: String) {
        viewModelScope.launch {
            if (newPath != pathToSave) {
                playlistRepository.deleteImageFromPrivateStorage(pathToSave)
                pathToSave = playlistRepository.saveImageToPrivateStorage(Uri.parse(newPath))
            }

            playlistRepository.updatePlaylist(
                Playlist(
                    playlistIdFromArgument,
                    playlistName,
                    playlistDescription,
                    pathToSave,
                    0
                )
            )
        }
    }

    fun currentMode() = isCreatingMode
}