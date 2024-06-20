package com.example.yp_playlist_maker

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.yp_playlist_maker.di.dataModule
import com.example.yp_playlist_maker.di.domainModule
import com.example.yp_playlist_maker.di.viewModelModule
import com.example.yp_playlist_maker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    private val settingsInteractor: SettingsInteractor by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            Log.d("MyApp", "Start Koin")
            androidContext(androidContext = this@MyApp)
            modules(dataModule, domainModule, viewModelModule)
        }

        val themeIsDark = settingsInteractor.retrieveIsDarkTheme()

        if (themeIsDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}