package com.example.yp_playlist_maker.sharing.domain

interface SharingRepository {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun shareString(message: String)
}