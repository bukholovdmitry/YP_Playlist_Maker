package com.example.yp_playlist_maker.media.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.media.data.PlaylistInfoState
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistInteractor
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistInteractor
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewModel(
    private val tracksInPlaylistInteractor: TracksInPlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private lateinit var currentPlaylistInfoState: PlaylistInfoState
    private val context: Context by KoinJavaComponent.inject(Context::class.java)

    private val statePlaylistLiveData = MutableLiveData<PlaylistInfoState>()

    fun observeState(): LiveData<PlaylistInfoState> = statePlaylistLiveData

    private fun renderState(playlistInfoState: PlaylistInfoState) {
        statePlaylistLiveData.postValue(playlistInfoState)
    }

    fun loadPlaylistInfo(playlist: Playlist) {
        syncPlaylist(playlist.playlistId)
        viewModelScope.launch {
            tracksInPlaylistInteractor
                .getTracksInPlaylist(playlist.playlistId)
                .collect {
                    currentPlaylistInfoState =
                        PlaylistInfoState(currentPlaylistInfoState.playlist, it)
                    renderState(currentPlaylistInfoState)
                }
        }
    }

    private fun syncPlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylist(playlistId).collect {
                currentPlaylistInfoState = PlaylistInfoState(it, listOf())
                renderState(currentPlaylistInfoState)
            }
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            tracksInPlaylistInteractor.deleteIfUnusableTrack(
                playlistId = currentPlaylistInfoState.playlist.playlistId,
                track.trackId
            )
        }
        viewModelScope.launch {
            tracksInPlaylistInteractor.deleteTrackFromPlaylist(
                playlistId = currentPlaylistInfoState.playlist.playlistId,
                track
            )
        }
    }

    fun sharePlaylist() {
        if (currentPlaylistInfoState.tracks.isEmpty()) {
            Toast.makeText(context, R.string.playlist_share_message, Toast.LENGTH_SHORT).show()
        } else {
            var message = context.resources.getQuantityString(
                R.plurals.count_of_track_numbers,
                currentPlaylistInfoState.tracks.size,
                currentPlaylistInfoState.tracks.size
            ) + "\n"
            currentPlaylistInfoState.tracks.mapIndexed { index, track ->
                val trackTime =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

                message += "${index + 1}.${track.artistName} - ${track.trackName} ($trackTime) \n".format(
                    track.artistName,
                    track.trackName,
                    trackTime
                )
            }
            sharingInteractor.shareString(message)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            tracksInPlaylistInteractor.deleteIfUnusableTracks(currentPlaylistInfoState.playlist.playlistId)
        }
        viewModelScope.launch {
            Log.d("Delete", "deleteTracksFromPlaylist ${currentPlaylistInfoState.playlist}")
            tracksInPlaylistInteractor.deleteAllTracksFromPlaylist(currentPlaylistInfoState.playlist.playlistId)
        }
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(currentPlaylistInfoState.playlist)
        }
    }

    fun getPlaylist() = currentPlaylistInfoState.playlist
}