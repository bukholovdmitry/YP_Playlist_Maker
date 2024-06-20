package com.example.yp_playlist_maker.sharing.domain

class SharingInteractorImpl(private val repository: SharingRepository) : SharingInteractor {
    override fun shareApp() {
        repository.shareApp()
    }

    override fun openTerms() {
        repository.openTerms()
    }

    override fun openSupport() {
        repository.openSupport()
    }

    override fun shareString(message: String) {
        repository.shareString(message)
    }
}