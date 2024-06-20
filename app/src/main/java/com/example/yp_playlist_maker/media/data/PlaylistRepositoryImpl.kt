package com.example.yp_playlist_maker.media.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.yp_playlist_maker.media.data.db.AppDatabase
import com.example.yp_playlist_maker.media.data.db.converters.PlaylistDbConvertor
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistEntity
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.sql.Timestamp

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val context: Context
) : PlaylistRepository {
    override fun playlists(): Flow<List<Playlist>> = flow {
        appDatabase.playlistDao().getPlaylists().collect {
            emit(convertFromPlaylistEntity(it))
        }
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        deleteImageFromPrivateStorage(playlist.pathImage)
        appDatabase.playlistDao().deletePlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylist(playlistId: Int): Flow<Playlist> = flow {
        appDatabase.playlistDao().getPlaylist(playlistId).collect {
            Log.d("PlaylistRepositoryImpl Playlist", playlistDbConvertor.map(it).toString())
            Log.d("PlaylistRepositoryImpl PlaylistEntity", it.toString())
            emit(playlistDbConvertor.map(it))
        }
    }

    override fun saveImageToPrivateStorage(uri: Uri): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString())
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file =
            File(filePath, uri.toString().split("/").last() + Timestamp(System.currentTimeMillis()))
        val inputStream = context.contentResolver?.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        Log.d("saveImageToPrivateStorage", "SaveImage from $uri to ${file.absolutePath}")
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.absolutePath
    }

    override fun deleteImageFromPrivateStorage(path: String) {
        val filePath = File(path)
        filePath.delete()
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlistEntity -> playlistDbConvertor.map(playlistEntity) }
    }
}