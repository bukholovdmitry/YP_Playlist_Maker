package com.example.yp_playlist_maker.sharing.data

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.sharing.domain.SharingRepository

class SharingRepositoryImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingRepository {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun shareString(message: String) {
        externalNavigator.shareLink(message)
    }

    private fun getShareAppLink(): String {
        return ContextCompat.getString(context, R.string.uri_practicum_android_developer)
    }

    private fun getTermsLink(): String {
        return ContextCompat.getString(context, R.string.uri_user_agreement)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            ContextCompat.getString(context, R.string.mailto),
            ContextCompat.getString(context, R.string.dev_email),
            ContextCompat.getString(context, R.string.subject_email),
            ContextCompat.getString(context, R.string.message_email)
        )
    }
}