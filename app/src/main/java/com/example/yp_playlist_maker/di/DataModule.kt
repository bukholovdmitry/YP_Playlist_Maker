package com.example.yp_playlist_maker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.example.yp_playlist_maker.media.data.PlaylistRepositoryImpl
import com.example.yp_playlist_maker.media.data.TracksInPlaylistRepositoryImpl
import com.example.yp_playlist_maker.media.data.db.AppDatabase
import com.example.yp_playlist_maker.media.data.db.converters.PlaylistDbConvertor
import com.example.yp_playlist_maker.media.data.db.converters.TrackDbConvertor
import com.example.yp_playlist_maker.media.domain.db.PlaylistRepository
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistRepository
import com.example.yp_playlist_maker.player.data.FavoriteTracksRepositoryImpl
import com.example.yp_playlist_maker.player.domain.PlayerRepository
import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksRepository
import com.example.yp_playlist_maker.player.domain.impl.PlayerRepositoryImpl
import com.example.yp_playlist_maker.search.data.NetworkClient
import com.example.yp_playlist_maker.search.data.network.ITunesApi
import com.example.yp_playlist_maker.search.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.search.domain.TracksHistoryRepository
import com.example.yp_playlist_maker.search.domain.TracksRepository
import com.example.yp_playlist_maker.search.repository.THEME_PREFERENCES
import com.example.yp_playlist_maker.search.repository.TracksHistoryRepositoryImpl
import com.example.yp_playlist_maker.search.repository.TracksRepositoryImpl
import com.example.yp_playlist_maker.settings.data.SettingsRepositoryImpl
import com.example.yp_playlist_maker.settings.domain.SettingsRepository
import com.example.yp_playlist_maker.sharing.data.ExternalNavigator
import com.example.yp_playlist_maker.sharing.data.SharingRepositoryImpl
import com.example.yp_playlist_maker.sharing.domain.SharingRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    factory { Gson() }

    single<NetworkClient> { RetrofitNetworkClient(get()) }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(get(), get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            THEME_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single {
        ExternalNavigator(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(get(), get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    factory<Handler> {
        Handler(Looper.getMainLooper())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { TrackDbConvertor() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

    factory { PlaylistDbConvertor() }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

    single<TracksInPlaylistRepository> {
        TracksInPlaylistRepositoryImpl(get(), get())
    }
}
