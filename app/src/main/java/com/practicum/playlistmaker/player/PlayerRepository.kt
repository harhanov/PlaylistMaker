package com.practicum.playlistmaker.player

interface PlayerRepository {
    fun preparePlayer(prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int
}