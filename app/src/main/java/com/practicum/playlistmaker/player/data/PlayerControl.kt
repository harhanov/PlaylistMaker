package com.practicum.playlistmaker.player.data

interface PlayerControl {
    fun preparePlayer(prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int

}