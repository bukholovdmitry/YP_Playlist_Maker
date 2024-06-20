package com.example.yp_playlist_maker.search.repository

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.domain.TracksHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val THEME_PREFERENCES = "yp_playlist_saved_theme"
const val THEME_TEXT = "key_for_theme"
const val TRACK_KEY = "TRACK_KEY"
const val MAX_SAVED_TRACKS_HISTORY_SIZE = 10
const val SAVED_TRACKS_PREFERENCES = "yp_playlist_saved_tracks"

class TracksHistoryRepositoryImpl(val gson: Gson, val context: Context) : TracksHistoryRepository {

    private val sharedPreferences = context.getSharedPreferences(
        SAVED_TRACKS_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun clearSavedTracks() {
        sharedPreferences.edit().clear().apply()
    }

    override fun read(): List<Track> {
        Log.d("SearchHistory.read", "Read SharedPreferences")
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return ArrayList()
        Log.d("SearchHistory.read", "SharedPreferences is founded")

        val typeTokenTrack = object : TypeToken<ArrayList<Track>>() {}.type
        Log.d("SearchHistory.read", "Return ArrayList<Track> with saved tracks")
        return gson.fromJson<List<Track>>(json, typeTokenTrack)
    }

    override fun write(track: Track) {
        Log.d("SearchHistory.write", "Read current saving history")
        val savedTracksArray = ArrayList<Track>()
        savedTracksArray.addAll(this.read())
        Log.d("SearchHistory.write", "Remove repetitions")
        savedTracksArray.forEach { forEachTrack: Track ->
            if (forEachTrack.trackId == track.trackId) savedTracksArray.remove(
                forEachTrack
            )
        }

        //Если количество сохраняемых треков ВДРУГ изменится, то будут отображены только 10 последних добавленных треков
        while (savedTracksArray.size >= MAX_SAVED_TRACKS_HISTORY_SIZE) {
            Log.d("SearchHistory.write", "Remove items exceeding size")
            savedTracksArray.removeFirst()
        }
        savedTracksArray.add(track)
        Log.d("SearchHistory.write", "Adding {%s} to SharedPreferences".format(track.toString()))
        sharedPreferences.edit()
            .putString(TRACK_KEY, gson.toJson(savedTracksArray))
            .apply()
    }

}