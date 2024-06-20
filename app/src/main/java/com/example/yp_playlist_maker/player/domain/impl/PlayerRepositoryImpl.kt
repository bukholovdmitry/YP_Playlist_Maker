package com.example.yp_playlist_maker.player.domain.impl

import android.content.Context
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.player.domain.PlayerRepository

class PlayerRepositoryImpl(private val context: Context) : PlayerRepository {
    override fun getStartPosition(): String {
        return context.getString(R.string.audio_player_start_position)
    }
}