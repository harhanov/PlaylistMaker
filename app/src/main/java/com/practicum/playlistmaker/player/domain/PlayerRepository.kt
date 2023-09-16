package com.practicum.playlistmaker.player.domain

interface PlayerRepository {
    fun preparePlayer(playerTrack: TrackForPlayer, prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int
}