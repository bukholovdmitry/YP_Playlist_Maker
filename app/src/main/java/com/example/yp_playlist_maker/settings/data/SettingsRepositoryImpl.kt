package com.example.yp_playlist_maker.settings.data

import android.content.SharedPreferences
import com.example.yp_playlist_maker.search.repository.THEME_TEXT
import com.example.yp_playlist_maker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {
    override fun storeDarkTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_TEXT, isDarkTheme).apply()
    }

    override fun retrieveIsDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_TEXT, false)
    }
}