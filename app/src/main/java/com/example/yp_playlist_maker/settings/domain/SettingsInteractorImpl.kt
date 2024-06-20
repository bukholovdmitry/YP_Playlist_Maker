package com.example.yp_playlist_maker.settings.domain

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun storeDarkTheme(isDarkTheme: Boolean) {
        repository.storeDarkTheme(isDarkTheme = isDarkTheme)
    }

    override fun retrieveIsDarkTheme(): Boolean {
        return repository.retrieveIsDarkTheme()
    }
}