package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.PlayerRepository
import com.practicum.playlistmaker.player.domain.PlayerInteractor


class PlayerInteractorImpl(private val track: PlayerRepository): PlayerInteractor {


    override fun start() {
        track.start()
    }

    override fun pause() {
        track.pause()
    }

    override fun onDestroy() {
        track.onDestroy()
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        track.setOnCompletionListener {
            onComplete()
        }
    }

    override fun getCurrentTime(): Int {
        return track.getCurrentTime()
    }

    override fun preparePlayer(prepare: () -> Unit) {
        track.preparePlayer { prepare() }
    }
}