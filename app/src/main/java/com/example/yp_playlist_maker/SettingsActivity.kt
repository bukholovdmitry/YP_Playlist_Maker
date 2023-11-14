package com.example.yp_playlist_maker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackFromSettings = findViewById<TextView>(R.id.button_back_from_settings)
        buttonBackFromSettings.setOnClickListener{
            finish()
        }

        val switchDarkTheme = findViewById<SwitchCompat>(R.id.switch_dark_theme)
        switchDarkTheme.isChecked = (Configuration.UI_MODE_NIGHT_MASK and resources.configuration.uiMode) == Configuration.UI_MODE_NIGHT_YES
        switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
            else { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
        }

        val textViewShare = findViewById<TextView>(R.id.text_view_share)
        textViewShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.uri_practicum_android_developer))
                type = getString(R.string.share_app_text_plain)
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val textViewSendToSupport = findViewById<TextView>(R.id.text_view_send_to_support)

        textViewSendToSupport.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.mailto))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message_email))
            }
            startActivity(emailIntent)
        }

        val textViewUserAgreement = findViewById<TextView>(R.id.text_view_user_agreement)
        textViewUserAgreement.setOnClickListener {startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uri_user_agreement)))) }
    }
}