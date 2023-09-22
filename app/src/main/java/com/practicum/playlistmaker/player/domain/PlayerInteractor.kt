package com.practicum.playlistmaker.player.domain

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun setOnCompletionListener(onComplete: () -> Unit)
    fun getCurrentTime(): Int
    fun preparePlayer(playerTrack: TrackModel, prepare: () -> Unit)
}