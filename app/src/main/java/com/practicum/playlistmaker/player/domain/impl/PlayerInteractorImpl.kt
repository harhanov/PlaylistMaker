package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.TrackForPlayer


class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {


    override fun start() {
        playerRepository.start()
    }

    override fun pause() {
        playerRepository.pause()
    }

    override fun onDestroy() {
        playerRepository.onDestroy()
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerRepository.setOnCompletionListener {
            onComplete()
        }
    }

    override fun getCurrentTime(): Int {
        return playerRepository.getCurrentTime()
    }

    override fun preparePlayer(playerTrack: TrackForPlayer, prepare: () -> Unit) {
        playerRepository.preparePlayer(playerTrack, prepare)
    }

}