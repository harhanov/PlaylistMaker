package com.practicum.playlistmaker.player.data

import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.player.domain.TrackForPlayer

class PlayerRepositoryImpl(private val playerClient: PlayerControl) : PlayerRepository {
    override fun preparePlayer(playerTrack: TrackForPlayer, prepare: () -> Unit) {
        playerClient.preparePlayer(playerTrack, prepare)
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerClient.setOnCompletionListener(onComplete)
    }

    override fun start() {
        playerClient.start()
    }

    override fun pause() {
        playerClient.pause()
    }

    override fun onDestroy() {
        playerClient.onDestroy()
    }

    override fun getCurrentTime(): Int {
        return playerClient.getCurrentTime()
    }
}