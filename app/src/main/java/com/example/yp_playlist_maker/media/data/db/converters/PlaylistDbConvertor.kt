package com.example.yp_playlist_maker.media.data.db.converters

import com.example.yp_playlist_maker.media.data.db.entity.PlaylistEntity
import com.example.yp_playlist_maker.media.domain.db.Playlist

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.pathImage,
            playlist.countTracksInPlaylist
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.namePlaylistName,
            playlist.descriptionPlaylist,
            playlist.pathImage,
            playlist.countTracksInPlaylist
        )
    }
}