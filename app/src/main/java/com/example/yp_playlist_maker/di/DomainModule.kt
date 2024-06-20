package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.media.domain.db.PlaylistInteractor
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistInteractor
import com.example.yp_playlist_maker.media.domain.impl.PlaylistInteractorImpl
import com.example.yp_playlist_maker.media.domain.impl.TracksInPlaylistInteractorImpl
import com.example.yp_playlist_maker.player.domain.PlayerInteractor
import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksInteractor
import com.example.yp_playlist_maker.player.domain.impl.FavoriteTracksInteractorImpl
import com.example.yp_playlist_maker.player.domain.impl.PlayerInteractorImpl
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractor
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractorImpl
import com.example.yp_playlist_maker.search.domain.TracksInteractor
import com.example.yp_playlist_maker.search.domain.TracksInteractorImpl
import com.example.yp_playlist_maker.settings.domain.SettingsInteractor
import com.example.yp_playlist_maker.settings.domain.SettingsInteractorImpl
import com.example.yp_playlist_maker.sharing.domain.SharingInteractor
import com.example.yp_playlist_maker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module


val domainModule = module {
    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<TracksHistoryInteractor> {
        TracksHistoryInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    factory<TracksInPlaylistInteractor> {
        TracksInPlaylistInteractorImpl(get())
    }
}