package com.practicum.playlistmaker.player.data

import com.practicum.playlistmaker.player.domain.TrackForPlayer

interface PlayerControl {
    fun preparePlayer(track: TrackForPlayer, prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int

}