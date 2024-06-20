package com.example.yp_playlist_maker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.yp_playlist_maker.R

class ExternalNavigator(val context: Context) {
    fun shareLink(link: String) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = context.getString(R.string.share_app_text_plain)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        Log.d("ExternalNavigaror", "Open shareIntent")
        context.startActivity(
            Intent.createChooser(shareIntent, null)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
        )
    }

    fun openLink(link: String) {
        val shareLinkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        Log.d("ExternalNavigaror", "Open shareLinkIntent")
        context.startActivity(shareLinkIntent)
    }

    fun openEmail(emailData: EmailData) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(emailData.mailTo)
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        Log.d("ExternalNavigaror", "Open emailIntent")
        context.startActivity(emailIntent)
    }
}