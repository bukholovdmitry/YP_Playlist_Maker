package com.example.yp_playlist_maker.player.domain.impl

import com.example.yp_playlist_maker.player.domain.PlayerInteractor
import com.example.yp_playlist_maker.player.domain.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    override fun getStartPosition(): String {
        return playerRepository.getStartPosition()
    }
}