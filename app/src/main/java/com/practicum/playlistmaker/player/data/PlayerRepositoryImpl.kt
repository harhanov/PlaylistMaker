package com.practicum.playlistmaker.player.data

import com.practicum.playlistmaker.player.PlayerRepository

class PlayerRepositoryImpl(private val playerClient: MediaPlayerControl): PlayerRepository {
    override fun preparePlayer(prepare: () -> Unit) {
        playerClient.preparePlayer(prepare)
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